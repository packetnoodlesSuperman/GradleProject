1. Initialization 初始化阶段
   Gradle支持单项目和多项目构建，在初始化阶段Gradle决定哪些项目需要加入构建
   并为这些需要加入构建的项目分别创建Project实例，实质为执行settings.gradle脚本
2. 配置阶段
   配置阶段将整个build的Project及Task关系确定，它会建立一个有向图来描述Task之间
   的依赖关系，实质为解析每个被加入构建项目的build.gradle脚本
3. 执行构建
   每个项目都有一个个的task（任务），每个任务之间都有相应的关系。
   执行构建的工作就是执行工作的任务，在执行任务的过程中，gradle会把这个任务的依赖的任务逐个执行

Initialization   ---> HOOK --->   Configuration   ---> HOOK --->   Execution   --->  HOOK
                       |                                |                             |
            Before project evaluation           Task graph populated                   Build finished
         gradle beforeProject { project ->    gradle.taskGraph.whenReady { graph ->    gradle.buildFinished { result ->
                ···                                   ···                                      ···
         }                                    }                                        } 