import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.mozilla.javascript.DefiningClassLoader;

import java.io.IOException;

public class Test {
    /*
    Static block of a class will be executed whenever the class is instantiated
     */
    static {
        System.out.println("Original class >>> " + Thread.currentThread().getStackTrace()[1].getClassName());
    }

    public static void main(String[] args) throws IOException {
        // StaticBlock staticBlock = new StaticBlock();
        Test to = new Test();
        to.Do();
    }

    public void Do() {
        String code = "{ System.out.println(\"Modified class with evil code >>> \" + Thread.currentThread().getStackTrace()[1].getClassName()); }";

        ClassPool pool = ClassPool.getDefault();
        CtClass clazz = null;
        try {
            /*
            get() - Reads a class file from the source and returns a reference to the CtClass object representing that class file.
            */
            clazz = pool.get(Test.class.getName());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        /*
        Set class name
         */
        clazz.setName("DangerousClass");
        try {
            /*
            makeClassInitializer() - Makes an empty class initializer (static constructor).
            insertBefore() - Inserts bytecode at the beginning of the body.
            */
            clazz.makeClassInitializer().insertBefore(code);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }

        DefiningClassLoader loader = new DefiningClassLoader();
        Class cls = null;
        try {
            /* Creates class from bytecode */
            cls = loader.defineClass("DangerousClass", clazz.toBytecode());
        } catch (IOException | CannotCompileException e) {
            e.printStackTrace();
        }

        try {
            /* Calls newInstance() */
            cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}