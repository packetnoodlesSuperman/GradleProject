### Project的认识
1. 每个module对应一个Project, Project和"build.gradle"文件之间存在一对一关系
>  Gradle是根据目录中有没有build.gradle文件来判断这个目录是不是一个Project
2. 初始化阶段，会从settings.gradle中解析生成Projec
``` 
gradlew projects

打印结果：一个根项目和N个子项目
```
3. Gradle以树的形式管理Project,最外层有一个根Project,在它下面有其它的几个子Project
   每个Project都是在build.gradle中去配置和管理的，这些build.gradle最终会被Gradle编译为Project字节码
4. 根Project是用来统筹管理所有的子Project的，而每个子Project都对应了一个输出
   比如我们的app module的类型是application的，那么它最终就对应生成了一个APK文件,
   是android library类型的，最终会生成一个aar文件，java library类型的生成一个jar文件等等        

### Project的自带属性
```
//所以所有的Project都需要一个默认的build.gradle文件，Gradle默认从该文件读取配置信息
String DEFAULT_BUILD_FILE = "build.gradle"
//路径分割符，如windows文件路径使用斜杠分割 
String PATH_SEPARATOR = ":"
//默认的build输出文件夹，默认build产生的apk等产物在此目录
String DEFAULT_BUILD_DIR_NAME = "build";
//在此属性文件中可修改一些Gradle默认的属性,也可扩展属性
String GRADLE_PROPERTIES = "gradle.properties";

//Gradle的扩展属性


//另外一个local.properties，用于构建系统配置本地环境属性，
//例如SDK安装路径。由于该文件的内容由AS自动生成并且专用于本地开发者环境
//因此不应手动修改该文件，或将其纳入版本控制系统

```

### Project的API
```
String  --> absoluteProjectPath(String path)    将名称转换成项目路径，解析相对于此项目的名称
void    --> afterEvaluate(Closure closure)      添加一个闭包，在此工程后立即调用  
```