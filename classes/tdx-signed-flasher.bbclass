# Use base class from meta-toradex-security:
inherit tdx-signed

# Enable Fastboot protection:
TDX_UBOOT_HARDENING_ENABLE_FB_PROT = "1"

# Extra command categories required for WIC erasing/flashing:
#
# - CMD_CAT_MMC_CONTROL is required by the "mmc partconf" command.
# - CMD_CAT_MMC_WRITE is required to allow the "mmc erase" command; unlike the
#   Fastboot erase command, it gives granular control on which areas will be
#   erased.
#
TDX_SECBOOT_WL_ALLOW_CLOSED_CATEG_DEFAULT = "CMD_CAT_ALL_SAFE CMD_CAT_MMC_CONTROL CMD_CAT_MMC_WRITE"

# Configurations aimed at SDP loading (relevant to iMX6ULL/7).
TDX_IMX_HAB_SDP_CONFIGS = "*"
