package com.bob.plugin;

import org.gradle.api.DefaultTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.Input
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * https://github.com/OnlyTerminator/AndroidResourcesCheck/blob/master/buildsrc/src/main/groovy/com/buildsrc/plugin/GeekPlugin.groovy
 */
class CheckSourcePlugin implements Plugin<Project> {
    final HashMap<String, ResourceInfo> stringMap = new HashMap<>();
    final HashMap<String, ResourceInfo> stringError = new HashMap<>();
    final HashMap<String, ResourceInfo> colorMap = new HashMap<>();
    final HashMap<String, ResourceInfo> colorError = new HashMap<>();
    final HashMap<String, ResourceInfo> dimesMap = new HashMap<>();
    final HashMap<String, ResourceInfo> dimesError = new HashMap<>();
    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
    @Override
    void apply(Project project) {


        CheckSourceTask task = project.getTasks().create("checkResources", CheckSourceTask.class);
        task.doLast {
            checkString(project, task)
        }
    }

    void checkString(Project project, CheckSourceTask task) {
        if (task.checkString){
            fixedThreadPool.execute({
                filterString(project)
                if (!stringError.isEmpty()) {
                    writeObject(stringError, "stringError.txt")
                } else {
                    File file = new File("ResourcesError"+File.separator+"stringError.txt")
                    if(file.exists()){
                        file.delete()
                    }
                }
                stringError.clear()
                stringMap.clear()
            })
        }
    }

    void filterString(Project project) {
        System.out.println("project.childProjects" + project.childProjects.size() + "")
        if (project.childProjects.size() > 1) {
            project.childProjects.each {
                filterString(it.value)
            }
            return
        }
        File file = new File("${project.projectDir}/src/main/res/values/strings.xml")
        if (!file.exists()) {
            return
        }
        System.out.println("file" + file.getAbsoluteFile())
        def list = new XmlParser().parse(file)
        list.each {
            String name = it.@name
            String values = it.text()
            if (!"app_name".equals(name)) {
                System.out.println("name --> " + name)
                System.out.println("values --> " + values)
                if (stringMap.containsKey(name)) {
                    ResourceInfo v = stringMap.get(name)
                    if (!values.equals(v.values)) {
                        stringError.put(v.values, v.toString())
                        stringError.put(values, new ResourceInfo(name, values, file.absoluteFile.toString()).toString())
                    }
                } else {
                    stringMap.put(name, new ResourceInfo(name, values, file.absoluteFile.toString()))
                }
            }
        }
    }

    void writeObject(HashMap<String,String> map, String path) {
        System.out.println("writeObject")
        try {
            File fileDir = new File("ResourcesError")
            if(!fileDir.exists()){
                fileDir.mkdir()
            }
            FileOutputStream outStream = new FileOutputStream("ResourcesError"+File.separator+path)
            OutputStreamWriter objectOutputStream = new OutputStreamWriter(outStream,"utf-8")

            for (String value : map.values()) {
                objectOutputStream.write(value+"\t\n")
            }
            objectOutputStream.flush()
            outStream.flush()
            outStream.close()
        } catch (FileNotFoundException e) {
            e.printStackTrace()
        } catch (IOException e) {
            e.printStackTrace()
        }
    }


    static class CheckSourceTask extends DefaultTask {

        boolean checkString = false
        boolean checkColor = false;
        boolean checkDimen = false

        @Input
        void checkString(boolean flag) {
            this.checkString = flag;
        }

        @Input
        void checkColor(boolean flag) {
            this.checkColor = flag;
        }

        @Input
        void checkDimen(boolean flag) {
            this.checkDimen = flag;
        }
    }

    static final class ResourceInfo {
        public final String name
        public final String values
        public final String path

        ResourceInfo(String name, String values, String path) {
            this.name = name
            this.values = values
            this.path = path
        }
    }
}
