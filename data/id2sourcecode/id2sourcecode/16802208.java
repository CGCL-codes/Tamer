    public static void printLoadMethod(PrintStream ps, Class theclass) {
        String className = theclass.getSimpleName();
        ps.println("    protected " + className + " read" + className + "(KXmlParser parser) throws Exception {");
        ps.println("        " + className + " object = new " + className + "();");
        ps.println("        int count = parser.getAttributeCount();");
        ps.println("        for (int c=0;c<count;c++) {");
        ps.println("            String key = parser.getAttributeName(c);");
        ps.println("            String value = parser.getAttributeValue(c);");
        ArrayList<Method> simpleMethods = getMethods(theclass, true, true, true);
        int n = 0;
        for (Method m : simpleMethods) {
            Class param = m.getParameterTypes()[0];
            ps.println("            " + (n == 0 ? "" : "else ") + "if (\"" + paramName(m) + "\".equals(key)) {");
            if (param == String.class) {
                ps.println("                object." + m.getName() + "(value);");
            } else if (param == int.class) {
                ps.println("                object." + m.getName() + "( Integer.parseInt(value) );");
            } else if (param == double.class) {
                ps.println("                object." + m.getName() + "( Double.parseDouble(value) );");
            } else if (param == float.class) {
                ps.println("                object." + m.getName() + "( Float.parseFloat(value) );");
            } else if (param == boolean.class) {
                ps.println("                object." + m.getName() + "( \"true\".equals( value ) );");
            } else if (param == short.class) {
                ps.println("                object." + m.getName() + "( Short.parseShort( value ) );");
            } else if (param == long.class) {
                ps.println("                object." + m.getName() + "( Long.parseLong( value ) );");
            } else if (param == char.class) {
                ps.println("                object." + m.getName() + "( value.charAt(0) );");
            } else if (param == byte.class) {
                ps.println("                object." + m.getName() + "( Byte.parseByte(value) );");
            } else if (param == byte[].class) {
                ps.println("                object." + m.getName() + "( org.bouncycastle.util.encoders.Base64.decode(value) );");
            } else {
                throw new RuntimeException();
            }
            ps.println("            }");
            n++;
        }
        if (simpleMethods.size() > 0) {
            ps.println("            else {");
            ps.println("                System.out.println(\"unknown item found \"+key);");
            ps.println("            }");
        } else {
            ps.println("            System.out.println(\"unknown item found \"+key);");
        }
        ps.println("        }");
        ArrayList<Method> complexMethods = getMethods(theclass, true, false, true);
        if (complexMethods.size() > 0) {
            ps.println("        while (parser.nextTag() != KXmlParser.END_TAG) {");
            ps.println("            String name = parser.getName();");
            n = 0;
            for (Method m : complexMethods) {
                Class param = m.getParameterTypes()[0];
                ps.println("            " + (n == 0 ? "" : "else ") + "if (\"" + paramName(m) + "\".equals(name)) {");
                ps.println("                Object obj = null;");
                ps.println("                while (parser.nextTag() != KXmlParser.END_TAG) {");
                ps.println("                    if (obj!=null) { throw new IOException(); }");
                ps.println("                    obj = readObject(parser);");
                ps.println("                }");
                if (param.isArray()) {
                    ps.println("                " + param.getComponentType().getSimpleName() + "[] array = null;");
                    ps.println("                if (obj!=null) {");
                    ps.println("                    Object[] objects = (Object[])obj;");
                    ps.println("                    array = new " + param.getComponentType().getSimpleName() + "[objects.length];");
                    ps.println("                    System.arraycopy(objects,0,array,0,objects.length);");
                    ps.println("                }");
                    ps.println("                object." + m.getName() + "(array);");
                } else {
                    ps.println("                object." + m.getName() + "( (" + param.getSimpleName() + ")obj );");
                }
                ps.println("            }");
                n++;
                if (n == complexMethods.size()) {
                    ps.println("            else {");
                    ps.println("                System.out.println(\"unknown section: \"+name);");
                    ps.println("                parser.skipSubTree();");
                    ps.println("            }");
                }
            }
            ps.println("        }");
        } else {
            ps.println("        parser.skipSubTree();");
        }
        ps.println("        return object;");
        ps.println("    }");
    }
