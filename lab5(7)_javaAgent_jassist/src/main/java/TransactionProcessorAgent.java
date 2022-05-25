import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Arrays;

import javassist.*;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;

public class TransactionProcessorAgent {
    public static void premain(String agentArgument, Instrumentation instrumentation) throws NotFoundException, ClassNotFoundException {
        System.out.println("Agent Counter");

        ClassPool classPool = ClassPool.getDefault();
        CtClass objCls = classPool.getCtClass("java.lang.Object");
        CtConstructor constructor = objCls.getConstructors()[0];
        try {
            constructor.insertBefore("System.out.println(\"INSERTED: Allocating Object\");");
        } catch (CannotCompileException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        instrumentation.addTransformer(new ClassTransformer());
    }

    public static class ClassTransformer implements ClassFileTransformer {

        private static int count = 0;

        ClassTransformer() {}

        @Override
        public byte[] transform(ClassLoader loader,
                                String className,
                                Class<?> classBeingRedefined,
                                ProtectionDomain protectionDomain,
                                byte[] classfileBuffer) {
            // 1. Total number of classes loaded by JVM during the application lifetime
            System.out.println("AGENT: Load class: " + className.replaceAll("/", "."));
            System.out.printf("AGENT: Loaded %s classes%n", ++count);

            ClassPool classPool = ClassPool.getDefault();
            CtClass currentClass;
            try {
                // 3. Record all allocations
                currentClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
                CtConstructor [] constrs = currentClass.getConstructors();
                Arrays.stream(constrs).forEach(constructor -> {
                    try {
                        constructor.insertBefore("System.out.println(\"INSERTED: Allocating " + className + "\");");
                    } catch (CannotCompileException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                });
                // 4. Measure a time needed to complete each method in all classes defined in package nsu.fit.javaperf
                if (currentClass.getPackageName().startsWith("nsu.fit.javaperf")){
                    CtMethod[] methods = currentClass.getDeclaredMethods();
                    Arrays.stream(methods).forEach(method -> {
                        if ((method.getMethodInfo().getAccessFlags() & AccessFlag.STATIC) == 0){ // If method not static
                            try {
                                method.addLocalVariable("startTime", CtClass.longType);
                                method.insertBefore("startTime = System.currentTimeMillis();\n");
                                method.insertAfter("""
                                    long finish = System.currentTimeMillis();
                                    long timeElapsed = finish - startTime;
                                    System.out.println("INSERTED: Elapsed Time: " + timeElapsed + "ms");""");
                            } catch (CannotCompileException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
                return currentClass.toBytecode();
            } catch (IOException | CannotCompileException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    /*public static class ClassObjectTransformer implements ClassFileTransformer {

        @Override
        public byte[] transform(ClassLoader loader,
                                String className,
                                Class<?> classBeingRedefined,
                                ProtectionDomain protectionDomain,
                                byte[] classfileBuffer) {

            ClassPool classPool = ClassPool.getDefault();
            CtClass currentClass;
            try {
                // 3. Record all allocations
                currentClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
                CtConstructor[] constrs = currentClass.getConstructors();
                Arrays.stream(constrs).forEach(constructor -> {
                    try {
                        constructor.insertBefore("System.out.println(\"INSERTED: Allocating " + className + "\");");
                    } catch (CannotCompileException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/


}
