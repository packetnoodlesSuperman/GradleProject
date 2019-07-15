groovy文件与gradle文件区别
https://blog.csdn.net/doctor_who2004/article/details/57455850

### Gradle的脚本认识 （区别.groovy文件与.Gradle文件）
1. build.gradle是Groovy脚本 
   比如：apply plugin: 'java'其实，它是Groovy脚本，即：apply([plugin: 'java'])
   当然，build.gradle中的脚本使用了最简化的Groovy语法

2. gradle脚本中的变量类型
   java的byte、short、int、long、float、double、char、boolean、String、Object等
```
Groovy中最终都是对象类型

int x = 10
println x.class //结果为：class java.lang.Integer

double y = 3.14
println y.class  //结果为：class java.lang.Double
```

3. 变量的定义：强类型定义方式和弱类型def定义方式
```
def x1 = 10
def y1 = 3.14
def str = 'groovy study'

println x1.class  //class java.lang.Integer
println y1.class  //class java.math.BigDecimal
println str.class //class java.lang.String
//强类型定义及定义的时候写明变量的类型，而def则由编译器自行推导变量的类型
```

4. Groovy是基于Java并拓展了Java 运行在JVM上 (Groovy完全兼容Java的语法)
   执行Groovy脚本时 Groovy会先变成Java字节码，然后执行在JVM上
   既可以用于面向对象编程，有可以作为脚本语言
   Groovy是一种动态语言 支持DSL语法（Domain Specific Language）
 
5. gradle文本格式特性 --> 如下
   分号是可选的   （分号可以省略） 
   类、方法默认是public的
   编译器给属性自动添加getter/setter方法
   属性可以直接用点号获取
   == 等同于equals() 不会有NullPointException
   最后一个表达式的值被作为返回值
   assert语句 随处可见的断言操作
   可选的括号 调用方法的时候 有参数 括号可以不写
   字符串的三种表达形式 单引号 双引号 三引号