# meta-toradex-flasher

This layer provides a standalone tool for flashing raw (WIC) images onto Toradex devices (System-On-Modules). It serves as an alternative to the Toradex Easy Installer.

It provides a small distro called `flasher` along with a recipe that produces a self-contained ZIP archive containing the bootloader binary capable of performing flashing operations plus the flashing scripts.

## Layer dependencies

This layer depends on:

```
URI: https://github.com/openembedded/openembedded-core.git
layers: meta
branch: scarthgap
revision: HEAD

URI: https://github.com/openembedded/meta-openembedded.git
layers: meta-oe
branch: scarthgap
revision: HEAD

URI: https://git.yoctoproject.org/meta-arm
layers: meta-arm meta-arm-toolchain
branch: scarthgap
revision: HEAD

URI: https://git.yoctoproject.org/meta-ti
layers: meta-ti-bsp meta-ti-extras
branch: scarthgap
revision: HEAD

URI: https://github.com/Freescale/meta-freescale.git
branch: scarthgap
revision: HEAD

URI: https://github.com/Freescale/meta-freescale-3rdparty.git
branch: scarthgap
revision: HEAD

URI: https://git.toradex.com/meta-toradex-bsp-common.git
branch: scarthgap
revision: HEAD

URI: https://git.toradex.com/meta-toradex-nxp.git
branch: scarthgap
revision: HEAD

URI: https://git.toradex.com/meta-toradex-ti.git
branch: scarthgap
revision: HEAD

URI: https://github.com/toradex/meta-toradex-security.git
branch: scarthgap
revision: HEAD
```

## Contributing

This layer is maintained by Toradex.

## License

All metadata is MIT licensed unless otherwise stated. Source code and binaries included in tree for individual recipes is under the LICENSE stated in each recipe (.bb file) unless otherwise stated.

This README document is Copyright (C) 2026 Toradex AG.
