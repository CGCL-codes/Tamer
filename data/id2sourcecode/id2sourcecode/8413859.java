    public static void printLoadMethod(PrintStream ps, Class theclass) {
        String className = theclass.getSimpleName();
        ps.println("    protected " + className + " read" + className + "(DataInputStream in,int size) throws IOException {");
        ps.println("        " + className + " object = new " + className + "();");
        ArrayList<Method> methods = getMethods(theclass, true);
        int n = 0;
        for (Method m : methods) {
            Class param = m.getParameterTypes()[0];
            ps.println("        if (size>" + n + ") {");
            if (param == int.class) {
                ps.println("            checkType(in.readInt() , TYPE_INTEGER);");
                ps.println("            object." + m.getName() + "( in.readInt() );");
            } else if (param == double.class) {
                ps.println("            checkType(in.readInt() , TYPE_DOUBLE);");
                ps.println("            object." + m.getName() + "( in.readDouble() );");
            } else if (param == float.class) {
                ps.println("            checkType(in.readInt() , TYPE_FLOAT);");
                ps.println("            object." + m.getName() + "( in.readFloat() );");
            } else if (param == boolean.class) {
                ps.println("            checkType(in.readInt() , TYPE_BOOLEAN);");
                ps.println("            object." + m.getName() + "( in.readBoolean() );");
            } else if (param == short.class) {
                ps.println("            checkType(in.readInt() , TYPE_SHORT);");
                ps.println("            object." + m.getName() + "( in.readShort() );");
            } else if (param == long.class) {
                ps.println("            checkType(in.readInt() , TYPE_LONG);");
                ps.println("            object." + m.getName() + "( in.readLong() );");
            } else if (param == char.class) {
                ps.println("            checkType(in.readInt() , TYPE_CHAR);");
                ps.println("            object." + m.getName() + "( in.readChar() );");
            } else if (param == byte.class) {
                ps.println("            checkType(in.readInt() , TYPE_BYTE);");
                ps.println("            object." + m.getName() + "( in.readByte() );");
            } else if (param != byte[].class && param.isArray()) {
                ps.println("            Object[] objects = (Object[])readObject(in);");
                ps.println("            " + param.getComponentType().getSimpleName() + "[] array=null;");
                ps.println("            if (objects!=null) {");
                ps.println("                array = new " + param.getComponentType().getSimpleName() + "[objects.length];");
                ps.println("                System.arraycopy(objects,0,array,0,objects.length);");
                ps.println("            }");
                ps.println("            object." + m.getName() + "(array);");
            } else {
                ps.println("            object." + m.getName() + "( (" + param.getSimpleName() + ")readObject(in) );");
            }
            ps.println("        }");
            n++;
        }
        ps.println("        if (size>" + n + ") {");
        ps.println("            skipUnknownObjects(in,size - " + methods.size() + ");");
        ps.println("        }");
        ps.println("        return object;");
        ps.println("    }");
    }
