    public static void main(String[] args) {
        int[] ia = new int[5];
        Object[] oa = new Object[2];
        Object[][] oaa = new Object[1][1];
        String[] sa = new String[3];
        String[][] saa = new String[1][1];
        String[][] saa2 = new String[2][];
        String[][][] saaa = new String[1][2][3];
        Object o = new Object();
        java.io.Serializable[] sera = new java.io.Serializable[1];
        Cloneable[] cloa = new Cloneable[1];
        StringBuffer[][] sbaa = new StringBuffer[1][1];
        Foo[] fooa = new Foo[1];
        FooChild[] fooca = new FooChild[1];
        Class[] ifs = String[].class.getInterfaces();
        is(ifs.length, 2, "String[] implements 2 interfaces");
        ok(ifs[0] == java.lang.Cloneable.class || ifs[1] == java.lang.Cloneable.class, "String[] implements Cloneable");
        ok(ifs[0] == java.io.Serializable.class || ifs[1] == java.io.Serializable.class, "String[] implements Serializable");
        is(String[].class.getModifiers(), 1041, "String[] is public final abstract");
        is(oa.getClass().getName(), "[Ljava.lang.Object;", "classname ref");
        is(ia.getClass().getName(), "[I", "classname primitive");
        is(ia.length, 5, "arraylength primitive");
        is(oa.length, 2, "arraylength ref");
        is(saa2.length, 2, "arraylength of saa2");
        saa2[1] = new String[4];
        is(saa2[1].length, 4, "arraylength of saa2[1]");
        is(saaa.length, 1, "arraylength of saaa");
        is(saaa[0].length, 2, "arraylength of saaa[0]");
        is(saaa[0][1].length, 3, "arraylength of saaa[0][1]");
        ok(oa.getClass().isArray(), "Object[].isArray");
        ok(ia.getClass().isArray(), "int[].isArray");
        ok(!o.getClass().isArray(), "!Object.isArray");
        ok(!o.getClass().isPrimitive(), "!Object.isPrimitive");
        is(oa.getClass().getComponentType().getName(), "java.lang.Object", "component ref");
        ok(!oa.getClass().getComponentType().isPrimitive(), "component ref !isPrimitive");
        is(ia.getClass().getComponentType().getName(), "int", "component primitive");
        ok(ia.getClass().getComponentType().isPrimitive(), "component primitive isPrimitive");
        ok(saa.getClass().getComponentType().equals(sa.getClass()), "component of String[][] equals String[]");
        ok(!saa.getClass().getComponentType().equals(oa.getClass()), "component of String[][] !equals Object[]");
        ok(saa[0].getClass().equals(saa.getClass().getComponentType()), "saa[0].getClass equals component of String[][]");
        test_store(sa, new Object(), false, "!store Object in String[]");
        test_store(sa, new String("test"), true, "store String in String[]");
        test_store(oa, new Object(), true, "store Object in Object[]");
        test_store(oa, new String("test"), true, "store String in Object[]");
        test_store(oaa, sa, true, "store String[] in Object[][]");
        test_store(saa, oa, false, "!store Object[] in String[][]");
        test_store(sera, sa, true, "store String[] in java.io.Serializable[]");
        test_store(cloa, sa, true, "store String[] in Cloneable[]");
        test_store(sbaa, sa, false, "!store String[] in StringBuffer[][]");
        test_store(fooa, new Foo(), true, "store Foo in Foo[]");
        test_store(fooa, new FooChild(), true, "store FooChild in Foo[]");
        test_store(fooca, new Foo(), false, "!store Foo in FooChild[]");
        test_store(fooca, new FooChild(), true, "store FooChild in FooChild[]");
        try {
            Object[] oa2 = (Object[]) sa;
            ok(true, "cast String[] to Object[]");
        } catch (ClassCastException x) {
            System.out.println("    ClassCastException");
            ok(false, "cast String[] to Object[]");
        }
        try {
            String[] sa2 = (String[]) oa;
            ok(false, "!cast Object[] to String[]");
        } catch (ClassCastException x) {
            System.out.println("    ClassCastException");
            ok(true, "!cast Object[] to String[]");
        }
        ok(sa instanceof String[], "String[] instanceof String[]");
        ok(sa instanceof Object[], "String[] instanceof Object[]");
        ok(!(oa instanceof String[]), "Object[] !instanceof String[]");
        ok(oa instanceof Object[], "Object[] instanceof Object[]");
        ok(oaa instanceof Object[], "Object[][] instanceof Object[]");
        ok(saa instanceof Object[], "String[][] instanceof Object[]");
        ok(sa instanceof java.io.Serializable, "String[] instanceof java.io.Serializable");
        ok(sa instanceof java.lang.Cloneable, "String[] instanceof java.lang.Cloneable");
        ok(sa instanceof java.lang.Object, "String[] instanceof java.lang.Object");
        ok(saa[0] instanceof java.io.Serializable, "saa[0] instanceof java.io.Serializable");
        ok(saa[0] instanceof java.lang.Cloneable, "saa[0] instanceof java.lang.Cloneable");
        ok(saa[0] instanceof java.lang.Object, "saa[0] instanceof java.lang.Object");
        test_clone();
        test_arraycopy();
    }
