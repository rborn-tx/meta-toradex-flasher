inherit deploy

FLASHER_UUU_DEPLOYDIR = "${DEPLOYDIR}/uuu-flasher"

do_deploy() {
    install -d ${FLASHER_UUU_DEPLOYDIR}
    install -m 0755 ${WORKDIR}/uuu-${PV}     ${FLASHER_UUU_DEPLOYDIR}/uuu
    install -m 0644 ${WORKDIR}/uuu-${PV}.exe ${FLASHER_UUU_DEPLOYDIR}/uuu.exe
}

addtask deploy before do_build after do_install
