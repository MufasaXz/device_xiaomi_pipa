# SPDX-License-Identifier: Apache-2.0
ifeq ($(TARGET_PIPA_MAINLINE),true)
TARGET_KERNEL_SOURCE := kernel/xiaomi/pipa-mainline
TARGET_KERNEL_CONFIG := defconfig sm8250.config pipa_android17.config
TARGET_KERNEL_CLANG_VERSION := r584948b
TARGET_KERNEL_DTB := qcom/sm8250-xiaomi-pipa.dtb
TARGET_DTB_LIST_WILDCARD := qcom/sm8250-xiaomi-pipa
BOARD_KERNEL_IMAGE_NAME := Image.gz
BOARD_KERNEL_SEPARATED_DTBO := false
AB_OTA_PARTITIONS := $(filter-out dtbo,$(AB_OTA_PARTITIONS))
# Stock DTBO compatibility is unresolved; this is a local build checkpoint.
# The existing vendor modules cannot be loaded into Linux 6.18.
BOARD_KERNEL_CMDLINE += firmware_class.path=/vendor/firmware
# Match mainline's UFS platform path so first-stage init creates global by-name
# links before loading the encrypted userdata mapping.
BOARD_KERNEL_CMDLINE += androidboot.boot_devices=soc@0/1d84000.ufshc
$(call soong_config_set_bool,XIAOMI_KONA,mainline_pipa,true)
TARGET_RECOVERY_FSTAB := $(DEVICE_PATH)/mainline/fstab.pipa.mainline
$(call soong_config_set_bool,recovery,target_recovery_uses_qti_drm,false)

BOARD_MESA3D_USES_MESON_BUILD := true
BOARD_MESA3D_GALLIUM_DRIVERS := freedreno
BOARD_MESA3D_VULKAN_DRIVERS := freedreno
BOARD_MESA3D_MESON_ARGS += -Dprecomp-compiler=system -Dmesa-clc=system -Dspirv-tools=disabled
$(call soong_config_set,minigbm,platform,msm)
# The SDM backend is for downstream Qualcomm DRM and gralloc. An empty
# selection leaves only the generic upstream DRM and client backends.
$(call soong_config_set,DRMHWC,backend,)

# Paired with the opt-in QSEECom/CMA kernel transport, never silently enabled.
ifeq ($(TARGET_PIPA_MAINLINE_QSEECOM),true)
$(call soong_config_set_bool,libion,dmaheap_impl,true)
endif

BOARD_VENDOR_SEPOLICY_DIRS += $(DEVICE_PATH)/mainline/sepolicy/vendor

# vendor.prop selects EGL/Vulkan too. Use a filtered, reviewable copy rather
# than two contradictory read-only properties in the generated vendor image.
TARGET_VENDOR_PROP := $(filter-out device/xiaomi/sm8250-common/vendor.prop,$(TARGET_VENDOR_PROP))
TARGET_VENDOR_PROP += $(DEVICE_PATH)/mainline/sm8250-vendor.prop
endif
