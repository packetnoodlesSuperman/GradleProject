package com.bob.plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariantOutput
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * https://www.cnblogs.com/rainboy2010/p/9235645.html
 */
class ButterKnifePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        if (!(project.plugins.hasPlugin(LibraryPlugin) || project.plugins.hasPlugin(AppPlugin))) {
            throw new IllegalStateException('Butterknife plugin can only be applied to android projects')
        }

        def variants
        if (project.plugins.hasPlugin(LibraryPlugin)) {
            variants = project.android.libraryVariants
        } else {
            variants = project.android.applicationVariants
        }


        project.afterEvaluate {
            variants.all {
                BaseVariantOutput output -> variant.outputs.each {
                    output.processResources.doLast {
                        File rDir = new File(sourceOutputDir, packageForR.replaceAll('\\.',
                                StringEscapeUtils.escapeJava(File.separator)))
                        File R = new File(rDir, 'R.java')
                        FinalRClassBuilder.brewJava(R, sourceOutputDir, packageForR, 'R2')
                    }
                }
            }
        }
    }

//    final static class FinalRClassBuilder {
//        private static final String SUPPORT_ANNOTATION_PACKAGE = "android.support.annotation";
//        private static final String[] SUPPORTED_TYPES = {
//            "array", "attr", "bool", "color", "dimen", "drawable", "id", "integer", "string"
//        };
//
//        private FinalRClassBuilder() {
//        }
//
//        static void brewJava(File rFile, File outputDir, String packageName, String className)
//                throws Exception {
//            CompilationUnit compilationUnit = JavaParser.parse(rFile);
//            TypeDeclaration resourceClass = compilationUnit.getTypes().get(0);
//
//            TypeSpec.Builder result =
//                    TypeSpec.classBuilder(className).addModifiers(PUBLIC).addModifiers(FINAL);
//
//            for (Node node : resourceClass.getChildrenNodes()) {
//                if (node instanceof TypeDeclaration) {
//                    addResourceType(Arrays.asList(SUPPORTED_TYPES), result, (TypeDeclaration) node);
//                }
//            }
//
//            JavaFile finalR = JavaFile.builder(packageName, result.build())
//                    .addFileComment("Generated code from Butter Knife gradle plugin. Do not modify!")
//                    .build();
//
//            finalR.writeTo(outputDir);
//        }
//
//        private static void addResourceType(List<String> supportedTypes, TypeSpec.Builder result,
//                                            TypeDeclaration node) {
//            if (!supportedTypes.contains(node.getName())) {
//                return;
//            }
//
//            String type = node.getName();
//            TypeSpec.Builder resourceType = TypeSpec.classBuilder(type).addModifiers(PUBLIC, STATIC, FINAL);
//
//            for (BodyDeclaration field : node.getMembers()) {
//                if (field instanceof FieldDeclaration) {
//                    addResourceField(resourceType, ((FieldDeclaration) field).getVariables().get(0),
//                            getSupportAnnotationClass(type));
//                }
//            }
//
//            result.addType(resourceType.build());
//        }
//
//        private static void addResourceField(TypeSpec.Builder resourceType, VariableDeclarator variable,
//                                             ClassName annotation) {
//            String fieldName = variable.getId().getName();
//            String fieldValue = variable.getInit().toString();
//            FieldSpec.Builder fieldSpecBuilder = FieldSpec.builder(int.class, fieldName)
//                    .addModifiers(PUBLIC, STATIC, FINAL)
//                    .initializer(fieldValue);
//
//            if (annotation != null) {
//                fieldSpecBuilder.addAnnotation(annotation);
//            }
//
//            resourceType.addField(fieldSpecBuilder.build());
//        }
//
//        private static ClassName getSupportAnnotationClass(String type) {
//            return ClassName.get(SUPPORT_ANNOTATION_PACKAGE, capitalize(type) + "Res");
//        }
//
//        private static String capitalize(String word) {
//            return Character.toUpperCase(word.charAt(0)) + word.substring(1);
//        }
//    }
}