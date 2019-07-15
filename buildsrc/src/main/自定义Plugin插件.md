1. 新建一个叫 buildsrc 的Module， 官方建议module的名字一定要为 buildsrc

2. 修改build.gradle文件内容
```
    apply plugin: 'groovy'
    apply plugin: 'maven'
    dependencies {
        compile gradleApi()
        compile localGroovy()
    }
    repositories {
        mavenCentral()
    }
```

3. 清空其他文件夹目录 在src 新建main目录 
```
src 
    --- main
       ---groovy
            --- 包名
                --- XXXPlugin名字
        ---resources
            --- META_INF
                --- gradle-plugins
                    ---xxxPlugin.properties 
                    //注意这个文件可以随意命名 但是后面使用这个插件的时候 apply plugin:<properties 文件名>
                    //如Aspect apply plugin: 'aspect.plugin' 
.gitignore
build.gradle
.gradle       
```

4. properties 文件里指明Gradle插件的具体实现类(META_INF.gradle-plugins.xxxPlugin.properties 文件)
> implementation-class=XXXPlugin (全路径名称 是含包名的)

5. 写自定义插件 (注意插件是groovy文件 比如 CheckSourcePlugin.groovy)
