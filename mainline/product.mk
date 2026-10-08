# SPDX-License-Identifier: Apache-2.0
# Enable only with the development kernel that provides the memfd ioctl shim.
ifeq ($(TARGET_PIPA_MAINLINE),true)
# A source bring-up must not inherit the normal product's OFFICIAL label.
INFINITY_BUILD_TYPE := UNOFFICIAL

PRODUCT_PACKAGES += \
    init.pipa.mainline.memfd.rc \
    ueventd.pipa.mainline.rc \
    mesa3d \
    android.hardware.graphics.allocator-service.minigbm \
    mapper.minigbm \
    android.hardware.composer.hwc3-service.drm \
    android.hardware.memtrack-service.example

# Replace the conflicting defaults after the common product has been inherited.
PRODUCT_VENDOR_PROPERTIES := $(filter-out ro.hardware.egl=% ro.hardware.vulkan=%,$(PRODUCT_VENDOR_PROPERTIES))
PRODUCT_VENDOR_PROPERTIES += \
    ro.hardware.egl=mesa \
    ro.hardware.vulkan=freedreno \
    vendor.hwc.backend_override=generic

# Blobs remain available for dependent media/security components; they are not
# selected as EGL/Vulkan drivers. Do not replace Keymaster or encryption flags.
PRODUCT_COPY_FILES += \
    vendor/xiaomi/sm8250-common/proprietary/vendor/firmware/a650_zap.elf:$(TARGET_COPY_OUT_VENDOR)/firmware/qcom/sm8250/xiaomi/pipa/a650_zap.mbn \
    vendor/xiaomi/sm8250-common/proprietary/vendor/firmware/a650_gmu.bin:$(TARGET_COPY_OUT_VENDOR)/firmware/qcom/a650_gmu.bin \
    vendor/xiaomi/sm8250-common/proprietary/vendor/firmware/a650_sqe.fw:$(TARGET_COPY_OUT_VENDOR)/firmware/qcom/a650_sqe.fw
# Firmware layout from the pipa mainline port, pinned in dependencies.xml.
# This packages files only; DSP daemons/audio HALs still need integration.
PIPA_MAINLINE_FW := vendor/firmware/pipa-mainline/lib/firmware
PRODUCT_COPY_FILES += \
    $(PIPA_MAINLINE_FW)/novatek/nt36532_csot.bin:$(TARGET_COPY_OUT_VENDOR)/firmware/novatek/nt36532_csot.bin \
    $(PIPA_MAINLINE_FW)/novatek/nt36532_tianma.bin:$(TARGET_COPY_OUT_VENDOR)/firmware/novatek/nt36532_tianma.bin \
    $(PIPA_MAINLINE_FW)/awinic/aw88230_2113_pipa.bin:$(TARGET_COPY_OUT_VENDOR)/firmware/awinic/aw88230_2113_pipa.bin
PRODUCT_COPY_FILES += $(foreach fw,$(filter-out $(PIPA_MAINLINE_FW)/sm8250/xiaomi/pipa/a650_zap.mbn,$(wildcard $(PIPA_MAINLINE_FW)/sm8250/xiaomi/pipa/*)),\
    $(fw):$(TARGET_COPY_OUT_VENDOR)/firmware/qcom/sm8250/xiaomi/pipa/$(notdir $(fw)))
endif
