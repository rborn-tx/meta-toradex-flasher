# Using the flashing tool

## Unpacking

The flashing tool is typically distributed as a ZIP archive that must be extracted before use. For example, to extract the tool, run the following command (replace the file name as appropriate):

```
$ unzip toradex-flasher-apalis-imx6-1.0-devel-20260603194759.zip
```

## Flashing

After unpacking, the extracted files will reside within a directory named after the ZIP file (excluding the `.zip` extension). Navigate into this directory:

```
$ cd toradex-flasher-apalis-imx6-1.0-devel-20260603194759/
```

The prerequisites and detailed instructions for running the flashing tool are documented in the files `flash-linux.README` and `flash-windows.README`, depending on your operating system. Please consult the appropriate README for guidance.

To display the flash tool's help on Linux, use:

```
$ ./flash-linux.sh --help
```

To flash an OS image to a device, ensure that your device is properly connected to the host computer and placed into recovery mode. Then, execute the flashing script as follows:

```
$ sudo ./flash-linux.sh --wic <wic-image-to-flash> --bootloader <bootloader> [--spl <spl>]
```

Here is an explanation of the script arguments:

- `--wic <wic-image-to-flash>`: Path to the WIC image containing the OS to be flashed.
- `--bootloader <bootloader>`: Path to the bootloader binary to be flashed; usually distributed alongside the OS image being flashed.
- `--spl <spl>` (optional): Path to the SPL (Secondary Program Loader) binary; usually distributed alongside the OS image being flashed. Only include this option if an SPL is required for your machine.

**IMPORTANT**: Do not pass bootloader binaries from this flashing-tool package to `--bootloader` or to `--spl`. Files under `flash/` are used by the tool to boot the device into Fastboot. Supply the bootloader that was **built and released** with the WIC image you are flashing.


Ensure that all input artifacts are correctly identified. The following table provides guidance on the required files for each supported machine:

| Machine                | Bootloader Binary                      | SPL Binary              |
|------------------------|----------------------------------------|-------------------------|
| `aquila-imx95`         |                                        |                         |
| `aquila-am69`          |                                        |                         |
| `verdin-imx8mm`        | `imx-boot`, `flash.bin-${MACHINE}*`    | N/A                     |
| `verdin-imx8mp`        | `imx-boot`, `flash.bin-${MACHINE}*`    | N/A                     |
| `verdin-imx95`         |                                        |                         |
| `verdin-am62`          |                                        |                         |
| `verdin-am62p`         |                                        |                         |
| `toradex-smarc-imx8mp` |                                        |                         |
| `toradex-smarc-imx95`  |                                        |                         |
| `apalis-imx6`          | `u-boot.img`, `u-boot-${MACHINE}.img`  | `SPL`, `SPL-${MACHINE}` |
| `apalis-imx8`          |                                        |                         |
| `colibri-imx6`         | `u-boot.img`, `u-boot-${MACHINE}.img`  | `SPL`, `SPL-${MACHINE}` |
| `colibri-imx6ull-emmc` | `u-boot.imx`, `u-boot-${MACHINE}*.imx` | N/A                     |
| `colibri-imx7-emmc`    | `u-boot.imx`, `u-boot-${MACHINE}*.imx` | N/A                     |
| `colibri-imx8x`        |                                        |                         |

**Example:**  
To flash a Colibri iMX6 device, run:

```
$ sudo ./flash-linux.sh \
       --wic /path/to/os-artifacts/torizon-minimal-colibri-imx6.wic \
       --bootloader /path/to/os-artifacts/u-boot.img \
       --spl /path/to/os-artifacts/SPL
```

Carefully verify that you select the appropriate files for your specific hardware. Please refer to the documentation or contact support if you have any questions regarding the selection of image or bootloader files.
