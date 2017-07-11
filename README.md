# Clog  

[![](https://jitpack.io/v/dreamlivemeng/Clog.svg)](https://jitpack.io/#dreamlivemeng/Clog) 
[![](https://travis-ci.org/dreamlivemeng/Clog.svg?branch=master)](https://travis-ci.org/dreamlivemeng/Clog.svg?branch=master)
[![API](https://img.shields.io/badge/API-8%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=8)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/e1549d917e304d998cc9c06868464859)](https://www.codacy.com/app/dreamlivemeng/Clog?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=dreamlivemeng/Clog&amp;utm_campaign=Badge_Grade)
[![](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-dreamlivemeng-blue.svg)](http://blog.csdn.net/dreamlivemeng)


这是一个将android 崩溃日志写入到sd卡得工具类。  
This is a tool that write the Android crash log to the sd card.  
最低版本android2.2(8),已经适配到android7.0。  

目前对应Clog版本`0.0.1`.

## 导入方法

* Android Studio
	
	```
	 compile 'com.dreamlive.cn.clog:ClogLibrary:0.0.1'
	```
	



### 使用方法1
强烈建议使用方法1，因为不需要权限。  

在application的oncreate()配置
```java
  //android6.0也不需要动态sd权限，
  //将错误日志写入到sd卡,默认为Android/data/包名/files/logs下面放这个目录下主要是为了不需要权限
  CollectLog clog = CollectLog.getInstance();
  clog.init(this);

```

### 使用方法2
自定义日志存放路径
在application的oncreate()配置
```java
   //自定义日志存放路径,
   //这儿示例就只传了sd根目录下的dreamlivemeng(/storage/emulated/0/dreamlivemeng)，把错误日志写到这个目录下
  CollectLog clog = CollectLog.getInstance();
  clog.init(this, Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "dreamlivemeng");

```

## 混淆

```
#Clog
-dontwarn com.dreamlive.cn.**
-keep classcom.dreamlive.cn.**{*;}


```

