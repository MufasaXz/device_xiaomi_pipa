# Pipa Linux 6.18 Android 17 source checkpoint

This is experimental ROM integration. No tablet boot or hardware test has
passed, and no flashable release is approved. The normal downstream vendor
stack is incompatible with upstream MSM DRM even when the panel driver works.

## Selected stack

TARGET_PIPA_MAINLINE := true must be set before product inheritance. It selects
kernel/xiaomi/pipa-mainline with defconfig, sm8250.config and
pipa_android17.config; Mesa freedreno EGL/GLES and Turnip Vulkan; minigbm's MSM
allocator/mapper; and generic DRM HWC3. The common product excludes its
Qualcomm composer, allocator, mapper and memtrack services. DRMHWC's optional
SDM backend is unselected, and vendor.hwc.backend_override=generic makes the
runtime backend explicit. Proprietary graphics libraries remain available to
other vendor clients, but are not selected as the EGL/Vulkan implementation.

A650 GMU/SQE and pipa zap firmware are packaged at the upstream DT paths.
ODM ueventd grants access to DRM nodes and the system DMA heap. Vendor SELinux
labels the services, Mesa/mapper libraries and DRM nodes; the mainline option
requires neverallow checks. Android runtime enforcement, buffer imports,
fences, refresh rate, rotation, brightness and suspend still need device tests.
HDR, wide colour and protected-content capability claims are disabled pending
support. Camera/media clients using proprietary Qualcomm handles remain an
integration issue; the libion adapter does not implement protected memory.

The system_ext memfd init service sets sys.use_memfd at load-bpf-programs.
Pair it with CONFIG_MEMFD_CREATE=y and CONFIG_MEMFD_ASHMEM_SHIM=y, since the
old vendor API level otherwise makes libcutils choose ashmem.

## Reproduction

The published infinity_pipa_mainline product is a dedicated Infinity development
product. This workstation's existing infinity_pipa product also opts into the
mainline flag; its earlier local Infinity conversions are preserved separately.
Use dependencies.xml as a local manifest overlay on the matching Infinity
Android 17 source tree, including the separately published sm8250-common changes.
The overlay pins added dependencies and libion; see the commits on the matching
pipa-6.18.28-android17-v0.1 branches for device/common changes. It does not provide
an entire Android manifest or automatically solve vendor blobs and firmware.

From the ROM root:

    source build/envsetup.sh
    export OUT_DIR=out-pipa-mainline
    export USE_RBE=false USE_REWRAPPER=false SOONG_NINJA=ninja
    lunch infinity_pipa_mainline-user
    m bacon -j$(nproc --all)

The user-requested infinity_pipa-user lunch is used for the local build.
The label is forced UNOFFICIAL for development. A completed compile is not
permission to flash the generated OTA.

## Security and boot blockers

Existing hardware-backed Keymaster/Gatekeeper and wrappedkey_v0 fstab settings
are retained. Software KeyMint/Gatekeeper cannot substitute for the existing
hardware-bound keys. Do not format userdata or change encryption flags.

TARGET_PIPA_MAINLINE_QSEECOM selects the experimental libion DMA-heap adapter,
but is unset in this checkpoint. CONFIG_QSEECOM, the optional CMA exporter,
the QSEE device and its DT pools also remain disabled. Enabling the userspace
adapter alone cannot provide security: paired kernel/DT activation, heap
permissions/policy, listener/RPMB/TA validation and tablet tests are still needed.
Secure/protected allocation requests explicitly fail in the adapter.

Bootloader DTB/DTBO/vendor_boot compatibility remains unresolved. Merely excluding
dtbo from generated OTA partitions does not remove the stock bootloader overlay
or establish that the image will boot. The reference developer's davinci
bootloader/erase-dtbo procedure must not be applied to pipa.

No physical display/GPU, existing-data unlock, charging, audio, sensors, camera,
keyboard/pen, recovery or rollback validation has passed. No sideload kernel
ZIP is distributed while these release blockers remain.
