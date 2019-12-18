# jniTest

记录一下生成 android so 库的方法，根据搜索得来的结果，生成 so 文件可能有很多种方式，并且不同版本的 android studio 方式也不尽相同，下面的方式是我在 android studio 3.5.x 下测试的。

jni 中的源码文件来自 [react-native-pushy](https://github.com/reactnativecn/react-native-pushy/tree/master/android/jni)，版权参考其 [LICENSE](https://github.com/reactnativecn/react-native-pushy/blob/master/LICENSE)


# 一、生成头文件

1. 创建一个名为 `jniTest` 的文件夹

2. 在该文件夹下创建要使用 so 文件的 java 类，我这里是 [BSDiff.java](com/malacca/archives/BSDiff.java)（生成的 so 文件只能在这个类中加载使用）, 在文件中定义 loadLibrary 的 名称，并使用 `public static native` 定义 so 库需要导出的方法

3. 在 `jniTest` 目录执行 `javah -d jni com.malacca.archives.BSDiff`

    -d 设置了生成目录， 后面跟着刚才创建的 java 类名

    不出意外的话，将生成 [jni/com_malacca_archives_BSDiff.h](jni/com_malacca_archives_BSDiff.h)


# 二、实现 so 库

1. 创建 c 文件 [jni/BSDiff.c](jni/BSDiff.c#L54) 实现头文件 [jni/com_malacca_archives_BSDiff.h](jni/com_malacca_archives_BSDiff.h#L15) 方法

2. 创建 [jni/Android.mk](jni/Android.mk)，使用 `LOCAL_SRC_FILES` 导入 c 源码，注意里面的 `LOCAL_MODULE` 与 [java](com/malacca/archives/BSDiff.java#L6) 类中保持一致

3. 创建 [jni/Application.mk](jni/Application.mk)，指明要生成的平台，最简单的，设置为 `all`（即所有平台），如需指定，可设置为如： `armeabi-v7a x86` (用空格隔开)

# 三、生成 so 文件

搞定以上步骤后 

`cd jni`  
`ndk-build`

会生成 [libs](libs) 和 [obj](obj) 目录，其中 libs 目录下就是我们要的 so 文件了


# 四、备注

1、`Android Studio -> Preferences -> Appearance & Behavior -> System Settings->Android SDK -> SDK Tools` 确认安装了 `LLDB` 、`NDK`、`CMake`

2、系统 Path 确认导入了 android 工具，如：

```
export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/tools
export PATH=$PATH:$ANDROID_HOME/tools/bin
export PATH=$PATH:$ANDROID_HOME/platform-tools
export PATH=$PATH:$ANDROID_HOME/build-tools/28.0.3
export PATH=$PATH:$ANDROID_HOME/ndk/20.1.5948944
```