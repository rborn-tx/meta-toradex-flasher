SUMMARY = "Toradex Flasher - standalone bootloader flashing tool"
DESCRIPTION = "Packages the bootloader binary (virtual/bootloader) and \
flashing scripts into a ZIP archive that serves as a self-contained \
flashing tool for Toradex System-on-Modules."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

# NOTE: recovery/uuu.auto is taken from meta-toradex-tezi only changing the name
# of the bootloader binaries to match FLASHER_BOOTLOADER_BINARIES.
SRC_URI = " \
    file://flash-linux.sh \
    file://flash-windows.bat \
    file://flash-linux.README \
    file://flash-windows.README \
    file://flash/fastboot.uuu \
    file://flash/erase-boot.uuu \
    file://flash/erase-user.uuu \
    file://flash/flash-all.uuu \
    file://flash/flash-spl.uuu \
    file://recovery/uuu.auto \
"

inherit deploy

DEPENDS = "zip-native"

# Space-separated list of bootloader binary filenames (relative to DEPLOY_DIR_IMAGE).
# A single filename is valid; machines with multi-part bootloaders list all parts.
FLASHER_BOOTLOADER_BINARIES ?= ""
FLASHER_BOOTLOADER_BINARIES:apalis-imx6 ?= "u-boot.img SPL"
FLASHER_BOOTLOADER_BINARIES:colibri-imx6 ?= "u-boot.img SPL"
FLASHER_BOOTLOADER_BINARIES:colibri-imx6ull-emmc ?= "u-boot.imx"
FLASHER_BOOTLOADER_BINARIES:colibri-imx7-emmc ?= "u-boot.imx"
FLASHER_BOOTLOADER_BINARIES:verdin-imx8mm ?= "imx-boot"
FLASHER_BOOTLOADER_BINARIES:verdin-imx8mp ?= "imx-boot"

# Pass extra parameters to SDP boot command (if building signed images).
# TODO: Consider extracting the address from U-Boot mkimage logs.
UUU_SDP_BOOT_EXTRA_ARGS ?= ""
UUU_SDP_BOOT_EXTRA_ARGS:mx6ull-generic-bsp:tdx-signed ?= "-dcdaddr 0x00910000 -cleardcd"
UUU_SDP_BOOT_EXTRA_ARGS:mx7-generic-bsp:tdx-signed ?= "-dcdaddr 0x00910000 -cleardcd"

# TODO: Allow switching between prod and dev.
FLASHER_ZIP_NAME_PROD ?= "${PN}-${MACHINE}-${PV}"
FLASHER_ZIP_NAME_DEV ?= "${PN}-${MACHINE}-${PV}-devel-${DATETIME}"
FLASHER_ZIP_NAME_DEV[vardepsexclude] = "DATETIME"
FLASHER_ZIP_NAME ?= "${FLASHER_ZIP_NAME_DEV}"

# This recipe only produces a deploy artifact; skip the standard
# compile / install / package pipeline.
do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"
do_package[noexec] = "1"
do_packagedata[noexec] = "1"
do_package_write_ipk[noexec] = "1"
do_package_write_rpm[noexec] = "1"
do_package_write_deb[noexec] = "1"

# Ensure the bootloader has been deployed before we try to package it.
do_flasher_zip[depends] = "virtual/bootloader:do_deploy uuu-bin:do_deploy"
do_flasher_zip[cleandirs] = "${WORKDIR}/flasher-staging"

do_flasher_zip() {
    # Guard: fail early when the machine has not declared any bootloader binaries.
    if [ -z "${FLASHER_BOOTLOADER_BINARIES}" ]; then
        bbfatal "FLASHER_BOOTLOADER_BINARIES is not set for MACHINE=${MACHINE}."
    fi

    staging="${WORKDIR}/flasher-staging"
    mkdir -p "${staging}/flash"

    # --- UUU script assembly ---
    # Each UUU template is prepended with machine-specific header lines from
    # uuu.auto (which BitBake resolves to the machine-specific variant when
    # available, or falls back to the generic recovery/uuu.auto).
    # This mirrors the logic in tezi-run-metadata-raw-flashing.inc.
    uuu_auto="${WORKDIR}/recovery/uuu.auto"
    uuu_ver=$(head -1 "${uuu_auto}")
    cfg_line=$(grep '^CFG: .*FB:' "${uuu_auto}" | head -1)
    cfg_line_num=$(grep -n '^CFG: .*FB:' "${uuu_auto}" | head -1 | cut -d: -f1)

    # fastboot.uuu: prepend all lines up to and including the CFG line so that
    # the full SDP boot sequence is present before entering Fastboot mode.
    head -n "${cfg_line_num}" "${uuu_auto}" > "${staging}/flash/fastboot.uuu"

    # Bootloader binaries will be kept in flash/ alongside the UUU scripts; strip
    # the ../ prefix that uuu.auto uses for the Tezi layout where they are one
    # level up.
    sed -E "s#^(SDP[SV]*: (boot|write) -f )\.\./#\1#" \
	-i "${staging}/flash/fastboot.uuu"

    if [ -n "${UUU_SDP_BOOT_EXTRA_ARGS}" ]; then
        sed "s|^\(SDP:.*boot.*\)|\1 ${UUU_SDP_BOOT_EXTRA_ARGS}|" \
            -i "${staging}/flash/fastboot.uuu"
    fi
    cat "${WORKDIR}/flash/fastboot.uuu" >> "${staging}/flash/fastboot.uuu"

    # All other UUU scripts only need the version line and CFG line as header.
    for tmpl in erase-boot erase-user flash-all flash-spl; do
        {
            printf '%s\n%s\n' "${uuu_ver}" "${cfg_line}"
            cat "${WORKDIR}/flash/${tmpl}.uuu"
        } > "${staging}/flash/${tmpl}.uuu"
    done

    # --- Static host scripts and documentation ---
    # Give each file the line endings its consumer needs, whatever the checkout
    # supplied: cmd.exe cannot resolve GOTO/CALL labels without CRLF, and a CR on
    # a shebang line is fatal. Stripping CR first keeps the conversion idempotent.
    to_crlf() {
        sed -e 's/\r$//' -e 's/$/\r/' "$1" > "$2"
    }

    to_lf() {
        sed -e 's/\r$//' "$1" > "$2"
    }

    to_lf   "${WORKDIR}/flash-linux.sh"         "${staging}/flash-linux.sh"
    chmod 0755 "${staging}/flash-linux.sh"
    to_crlf "${WORKDIR}/flash-windows.bat"      "${staging}/flash-windows.bat"
    chmod 0755 "${staging}/flash-windows.bat"
    to_lf   "${WORKDIR}/flash-linux.README"     "${staging}/flash-linux.README"
    chmod 0644 "${staging}/flash-linux.README"
    to_crlf "${WORKDIR}/flash-windows.README"   "${staging}/flash-windows.README"
    chmod 0644 "${staging}/flash-windows.README"

    # --- Bootloader binaries ---
    for binary in ${FLASHER_BOOTLOADER_BINARIES}; do
        bootloader="${DEPLOY_DIR_IMAGE}/${binary}"
        if [ ! -f "${bootloader}" ]; then
            bbfatal "Bootloader binary not found: ${bootloader}"
        fi
        install -m 0644 "${bootloader}" "${staging}/flash/"
    done

    # --- uuu host binaries ---
    install -m 0755 "${DEPLOY_DIR_IMAGE}/uuu-flasher/uuu"     "${staging}/flash/uuu"
    install -m 0644 "${DEPLOY_DIR_IMAGE}/uuu-flasher/uuu.exe" "${staging}/flash/uuu.exe"

    # --- Create ZIP archive ---
    # Rename the staging directory so its name becomes the top-level directory
    # inside the ZIP (matching the ZIP filename without the .zip extension).
    mv "${staging}" "${WORKDIR}/${FLASHER_ZIP_NAME}"
    cd "${WORKDIR}"
    zip -r "${WORKDIR}/${FLASHER_ZIP_NAME}.zip" "${FLASHER_ZIP_NAME}"
}

# Reason for dependencies:
#
# - do_prepare_recipe_sysroot: ensures dependencies are available (e.g. zip);
# - do_patch: has no sstate and forces do_unpack to run populating the workdir
#   with the required files for deployment.
#
addtask flasher_zip after do_prepare_recipe_sysroot do_patch before do_deploy

do_deploy() {
    install -m 0644 "${WORKDIR}/${FLASHER_ZIP_NAME}.zip" "${DEPLOYDIR}/"
}

addtask deploy after do_flasher_zip
