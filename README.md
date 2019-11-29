# StarGate

这是一个尝试使用 [jitpack](https://jitpack.io/) 打包 android libary 的项目，也可能会做一些有用的包放到项目中。

# 过程

以下内容与该项目本身无关，记录一下使用 jitpack 的方法，当然，最好的教程是 [官方文档](https://jitpack.io/docs/)，这里仅供参考

1. 创建一个项目
2. 修改项目 [build.gradle](build.gradle#L12) 添加依赖
3. 创建一个或多个 libary (如当前项目的 example/zchar)，配置 libary [build.gradle](sample/build.gradle#L3)
4. 创建项目 releases 版本号 (该步骤可选)
5. 前往 [官方](https://jitpack.io/) 使用 github 账号登陆, 选择自己的项目生成 (也可不登录，直接指明包地址生成即可)


# 使用

以下说明是将自己的 libary 提供给其他人用的使用方法

1. 在项目 `build.gradle` 的 `allprojects` 代码块添加 jitpack 仓库地址

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```


2. 在需要的地方添加引用，与 maven jcenter 方式一样


```
// 引用方式
dependencies {
    implementation 'com.github.User:Repo:Tag'
}

// 如：当前项目, 这会同时引入项目下所有包 (如当前项目的 example/zchar)
dependencies {
    implementation 'com.github.malacca:stargate:0.0.2'
}


// 也可明确指定要引入的包
dependencies {
    implementation 'com.github.malacca.stargate:zchar:0.0.2'
}
```

3. 可能有用的 [wiki](wiki)