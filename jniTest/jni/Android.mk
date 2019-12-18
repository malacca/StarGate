LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := bsdiff
LOCAL_SRC_FILES := \
	BSDiff.c \
	blocksort.c \
	bspatch.c \
	bzlib.c \
	crctable.c \
	compress.c \
	decompress.c \
	huffman.c \
	randtable.c

include $(BUILD_SHARED_LIBRARY)