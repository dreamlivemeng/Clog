# Clog  

这是一个将android 崩溃日志写入到sd卡得工具类。  
This is a tool that write the Android crash log to the sd card.  
最低版本android2.2(8),已经适配到android7.0。  

目前对应Clog版本`0.0.1`.

## 用法

* Android Studio
	
	```
	compile 'com.zhy:okhttputils:2.6.2'
	```
	
* Eclipse
	
	下载最新jar:[okhttputils-2\_6\_2.jar](okhttputils-2_6_2.jar?raw=true)

	注：需要同时导入okhttp和okio的jar，下载见：[https://github.com/square/okhttp](https://github.com/square/okhttp).


### 使用方法1
强烈建议使用方法1，因为不需要权限。  

在application的oncreate（）配置
```java
//android6.0也不需要动态sd权限， 将错误日志写入到sd卡,默认为Android/data/包名/files/logs下面，放这个目录下主要是为了不需要权限
  CollectLog clog = CollectLog.getInstance();
  clog.init(this);

```

### 使用方法2
自定义日志存放路径
在application的oncreate（）配置
```java
//自定义日志存放路径,这儿示例就只传了sd根目录下的dreamlivemeng(/storage/emulated/0/dreamlivemeng)，希望把错误日志写到这个目录
  CollectLog clog = CollectLog.getInstance();
  clog.init(this, Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "dreamlivemeng");

```

## 混淆

```
#okhttputils
-dontwarn com.dreamlive.cn.**
-keep classcom.dreamlive.cn.**{*;}


```

