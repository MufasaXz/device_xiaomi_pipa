# SPDX-License-Identifier: Apache-2.0
# Enable only with the development kernel that provides the memfd ioctl shim.
ifeq ($(TARGET_PIPA_MAINLINE),true)
PRODUCT_PACKAGES += init.pipa.mainline.memfd.rc
endif
