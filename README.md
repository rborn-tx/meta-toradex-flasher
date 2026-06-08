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

## Supported SoMs

| Machine Name         | Corresponding Toradex Module                   | Supported |
|----------------------|------------------------------------------------|-----------|
| aquila-imx95         | Aquila iMX95                                   | Not yet   |
| aquila-am69          | Aquila AM69/TDA4                               | Not yet   |
| verdin-imx8mm        | Verdin iMX8M Mini                              | Yes       |
| verdin-imx8mp        | Verdin iMX8M Plus                              | Yes       |
| verdin-imx95         | Verdin iMX95                                   | Not yet   |
| verdin-am62          | Verdin AM62                                    | Not yet   |
| verdin-am62p         | Verdin AM62P                                   | Not yet   |
| toradex-smarc-imx8mp | SMARC iMX8M Plus                               | Not yet   |
| toradex-smarc-imx95  | SMARC iMX95                                    | Not yet   |
| apalis-imx6          | Apalis iMX6                                    | Yes       |
| apalis-imx8          | Apalis iMX8                                    | Not yet   |
| colibri-imx6         | Colibri iMX6                                   | Yes       |
| colibri-imx6ull-emmc | Colibri iMX6ULL 1GB (equipped with eMMC flash) | Yes       |
| colibri-imx7-emmc    | Colibri iMX7D 1GB (equipped with eMMC flash)   | Yes       |
| colibri-imx8x        | Colibri iMX8X V1.0C or newer                   | Not yet   |

## Building the flashing tool

For build instructions see [docs/BUILD.md](docs/BUILD.md).

## Using the flashing tool

For usage instructions see [docs/USAGE.md](docs/USAGE.md).

## Contributing

This layer is maintained by Toradex.

## License

All metadata is MIT licensed unless otherwise stated. Source code and binaries included in tree for individual recipes is under the LICENSE stated in each recipe (.bb file) unless otherwise stated.

This README document is Copyright (C) 2026 Toradex AG.
