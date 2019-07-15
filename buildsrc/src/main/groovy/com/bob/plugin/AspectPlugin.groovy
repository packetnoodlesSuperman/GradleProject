package com.bob.plugin

import com.android.build.gradle.LibraryPlugin
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.tasks.compile.JavaCompile

class AspectPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.dependencies {
            implementation 'org.aspectj:aspectjrt:1.8.9'
        }
        final def log = project.logger
        log.error "========================";
        log.error "Aspectj切片开始编织Class!";
        log.error "========================";


        def variants
        if (project.plugins.withType(LibraryPlugin)) {
            variants = project.android.libraryVariants
        } else if (project.plugins.withType(ApplicationPlugin)) {
            variants = project.android.applicationVariants
        } else {
            return
        }

        variants.all { variant ->
            def javaCompile = variant.javaCompile
            javaCompile.doLast {
                String[] args = ["-showWeaveInfo",
                                 "-1.8",
                                 "-inpath", javaCompile.destinationDir.toString(),
                                 "-aspectpath", javaCompile.classpath.asPath,
                                 "-d", javaCompile.destinationDir.toString(),
                                 "-classpath", javaCompile.classpath.asPath,
                                 "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]
                log.debug "ajc args: " + Arrays.toString(args)

                MessageHandler handler = new MessageHandler(true);
                new Main().run(args, handler);
                for (IMessage message : handler.getMessages(null, true)) {
                    switch (message.getKind()) {
                        case IMessage.ABORT:
                        case IMessage.ERROR:
                        case IMessage.FAIL:
                            log.error message.message, message.thrown
                            break;
                        case IMessage.WARNING:
                            log.warn message.message, message.thrown
                            break;
                        case IMessage.INFO:
                            log.info message.message, message.thrown
                            break;
                        case IMessage.DEBUG:
                            log.debug message.message, message.thrown
                            break;
                    }
                }
            }
        }
    }


    def isApplication(Project project) {
        try {
            return project.property('isBuildModule').toBoolean()
        } catch (ignored) {
            return true
        }
    }
}