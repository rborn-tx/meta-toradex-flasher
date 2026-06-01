# Support operation in recovery mode (replicating TEZI's configuration):
SRC_URI:append = " \
    file://recovery.cfg \
    file://toradex-recovery-u-boot.dtsi \
"

# Explicitly enter Fastboot mode during boot on some machines (others have
# this logic hard-coded into U-Boot):
SRC_URI:append:k3 = " file://bootcmd-fb-usb0.cfg"
SRC_URI:append:mx8m-generic-bsp = " file://bootcmd-fb-usb0.cfg"
SRC_URI:append:mx9-generic-bsp = " file://bootcmd-fb-usb0.cfg"

# Define Fastboot device for flashing operations:
FASTBOOT_DEVICE_FRAG ?= ""
FASTBOOT_DEVICE_FRAG:apalis-imx6 ?= "file://fb-dev0.cfg"
FASTBOOT_DEVICE_FRAG:colibri-imx6 ?= "file://fb-dev0.cfg"
FASTBOOT_DEVICE_FRAG:colibri-imx6ull-emmc ?= "file://fb-dev0.cfg"
FASTBOOT_DEVICE_FRAG:colibri-imx7-emmc ?= "file://fb-dev0.cfg"
FASTBOOT_DEVICE_FRAG:verdin-imx8mm ?= "file://fb-dev0.cfg"
FASTBOOT_DEVICE_FRAG:verdin-imx8mp ?= "file://fb-dev2.cfg"

# Patches to export useful environment variables directly from U-Boot so the
# host scripts do not need to hard-code them:
FASTBOOT_PATCHES_UPSTREAM = " \
    file://0001-apalis-imx6-add-fastboot-env-variables-upstream.patch \
    file://0001-colibri-imx6-add-fastboot-env-variables-upstream.patch \
    file://0001-colibri-imx6ull-add-fastboot-env-variables-upstream.patch \
    file://0001-colibri-imx7-add-fastboot-env-variables-upstream.patch \
    file://0001-verdin-imx8mm-add-fastboot-env-variables-upstream.patch \
    file://0001-verdin-imx8mp-add-fastboot-env-variables-upstream.patch \
"
FASTBOOT_PATCHES_DOWNSTREAM = ""
FASTBOOT_PATCHES_DOWNSTREAM_K3 = ""

FASTBOOT_PATCHES = "${FASTBOOT_PATCHES_UPSTREAM}"
FASTBOOT_PATCHES:apalis-imx8 = "${FASTBOOT_PATCHES_DOWNSTREAM}"
FASTBOOT_PATCHES:colibri-imx8x = "${FASTBOOT_PATCHES_DOWNSTREAM}"
FASTBOOT_PATCHES:toradex-smarc-imx95 = "${FASTBOOT_PATCHES_DOWNSTREAM}"
FASTBOOT_PATCHES:verdin-imx95 = "${FASTBOOT_PATCHES_DOWNSTREAM}"
FASTBOOT_PATCHES:k3 = "${FASTBOOT_PATCHES_DOWNSTREAM_K3}"
FASTBOOT_PATCHES:k3r5 = ""

SRC_URI:append = " \
    ${FASTBOOT_DEVICE_FRAG} \
    ${FASTBOOT_PATCHES} \
"

SRC_URI:append:imx-generic-bsp = " \
    file://fb-malloc.cfg \
"

RAW_FLASHING_SUPPORTED ?= "0"
RAW_FLASHING_SUPPORTED:apalis-imx6 ?= "1"
RAW_FLASHING_SUPPORTED:colibri-imx6 ?= "1"
RAW_FLASHING_SUPPORTED:colibri-imx6ull-emmc ?= "1"
RAW_FLASHING_SUPPORTED:colibri-imx7-emmc ?= "1"
RAW_FLASHING_SUPPORTED:verdin-imx8mm ?= "1"
RAW_FLASHING_SUPPORTED:verdin-imx8mp ?= "1"

do_compile:prepend () {
    if [ "${RAW_FLASHING_SUPPORTED}" != "1" ]; then
        bbfatal 'RAW/WIC flashing is not supported on the current machine' \
                '(make RAW_FLASHING_SUPPORTED="1" to bypass this error check).'
    fi
    cp "${WORKDIR}/toradex-recovery-u-boot.dtsi" "${S}/arch/arm/dts/"
}
