# Pipa Linux 6.18 ROM integration checkpoint

This directory is development source, not a complete mainline product or a
flashable release. The normal product leaves TARGET_PIPA_MAINLINE unset and
does not install the memfd init file. Existing local Infinity product changes
are independent of this checkpoint.

Set TARGET_PIPA_MAINLINE := true in a dedicated development product before
inheriting device.mk to install init.pipa.mainline.memfd.rc. It forces
libcutils shared-memory allocations to memfd at load-bpf-programs, following
the mainline Android reference approach. Pair it with the development kernel's
CONFIG_MEMFD_CREATE=y and CONFIG_MEMFD_ASHMEM_SHIM=y. The current platform's
libcutils otherwise chooses ashmem for pipa's old vendor API level. Install the
script under system_ext so platform init sets the system property, without
granting vendor_init permission to change a restricted system property.

Kernel source: https://github.com/MufasaXz/kernel_xiaomi_pipa/tree/pipa-6.18.28-android17-v0.1
Reference: https://github.com/me-cafebabe-aosp-mainline/android_device_mainline_common/blob/03b3b227cc79caa2a2942e30d33fd304d0a3c172/docs/KERNEL_PATCHES.md

The flag adds only shared-memory compatibility. It does not select the 6.18
kernel, change boot images or replace graphics/security HALs. Mesa freedreno,
minigbm MSM and DRM hardware composer need a dedicated product with conflicting
Qualcomm graphics packages, properties and VINTF fragments removed. The local
external/mesa3d Soong files do not currently expose freedreno EGL/Turnip modules;
the reference uses a different external/mesa integration.

Security must retain hardware-backed Keymaster/Gatekeeper and existing
wrappedkey_v0 userdata. The reference's nonsecure KeyMint/software Gatekeeper
defaults are not a solution for this requirement. QSEECom kernel ABI and secure
DMA heaps remain prerequisites. Do not change the fstab encryption flags or
format data to bypass these blockers.

Boot DTB/DTBO/vendor_boot integration and actual Android/hardware testing are
still pending. The reference davinci bootloader and erase-dtbo instructions
are specific to davinci and must not be applied to pipa.
