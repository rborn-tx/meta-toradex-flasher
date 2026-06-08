# Building the flashing tool

The procedure for building this tool resembles the steps used to create a Toradex Reference Image, which can be found in [Build a Reference Image with Yocto Project/OpenEmbedded](https://developer.toradex.com/linux-bsp/os-development/build-yocto/build-a-reference-image-with-yocto-projectopenembedded/).

## Prerequisites

Before moving on, make sure these [prerequisites](https://developer.toradex.com/linux-bsp/os-development/build-yocto/build-a-reference-image-with-yocto-projectopenembedded/#prerequisites) are fulfilled.

To simplify the process, we recommend using a (CROPS) container for the build.

## Set up the shell environment

Fetch the necessary repositories for building the flashing tool with the commands below. The instructions here assume the `repo` workspace directory to be named `toradex-flasher`:

```
$ mkdir toradex-flasher/
$ cd toradex-flasher/
$ repo init -u https://github.com/rborn-tx/toradex-manifest.git -b scarthgap-7.x.y-flasher -m flasher/default.xml
$ repo sync
```

Then create a build directory and set up the environment by running:

```
$ . export
```

Every time you start a new shell session, you must go to the `repo` workspace directory (`toradex-flasher/`) and source the export script to make the build commands available to you (e.g. `bitbake`).

## Edit the build configuration file

Edit the `conf/local.conf` (relative to the build directory) file as necessary.

Make sure to specify the `MACHINE` variable. To do this, locate the corresponding line in the file and remove the comment marker to enable it.

Building an image for NXP based SoMs may require you to read and accept the NXP®/Freescale EULA available in `layers/meta-freescale/EULA`. You must state your acceptance by adding the following line to your `local.conf` file:

```
ACCEPT_FSL_EULA = "1"
```

### Signed builds

If building the tool for flashing **secure boot** devices, add this line to your `local.conf`:

```
INHERIT += "tdx-signed-flasher"
```

The class `tdx-signed-flasher` is a child of `tdx-signed` which is provided by layer [meta-toradex-security](https://github.com/toradex/meta-toradex-security). For using it properly, you'll need to set up the bootloader signing:

<!-- TODO: Add link for machines based on TI SoCs (when supported) -->
- For NXP-based SoMs: see [README-secure-boot-imx](https://github.com/toradex/meta-toradex-security/blob/scarthgap-7.x.y/docs/README-secure-boot-imx.md#configuring-habahab-support)

## Build the tool

To build the Toradex flasher tool image, execute in the build directory:

```
$ bitbake toradex-flasher
```

If the build completes successfully, the output will be located by default in the `deploy/images/${MACHINE}/` directory. The resulting file will be a zip archive named `toradex-flasher-${MACHINE}-*.zip`.
