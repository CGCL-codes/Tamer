    private void generateSetValueMethod(SourceWriter sw) {
        sw.println("public void setSyncValueFromWave(String memberName, Object valueObject) {");
        sw.indent();
        PropertyDescriptor[] props = PropertyUtils.getPropertyDescriptors(superClass);
        boolean firstProp = true;
        for (PropertyDescriptor pd : props) {
            Method read = pd.getReadMethod();
            Method write = pd.getWriteMethod();
            if (read != null && write != null) {
                if (!firstProp) {
                    sw.print("} else ");
                } else {
                    firstProp = false;
                }
                sw.println("if (memberName.equals(\"" + pd.getDisplayName() + "\")) {");
                sw.indent();
                Class<?> propType = pd.getPropertyType();
                if (propType.isPrimitive()) {
                    String type = propType.getName();
                    String parseMeType = "********* " + type + "  ";
                    if ("int".equals(type)) {
                        parseMeType = "Integer.parseInt";
                    } else if ("boolean".equals(type)) {
                        parseMeType = "Boolean.parseBoolean";
                    } else if ("byte".equals(type)) {
                        parseMeType = "Byte.parseByte";
                    } else if ("char".equals(type)) {
                        parseMeType = "(char)Integer.parseInt";
                    } else if ("double".equals(type)) {
                        parseMeType = "Double.parseDouble";
                    } else if ("float".equals(type)) {
                        parseMeType = "Float.parseFloat";
                    } else if ("long".equals(type)) {
                        parseMeType = "Long.parseLong";
                    } else if ("short".equals(type)) {
                        parseMeType = "Short.parseShort";
                    }
                    sw.println("super." + write.getName() + "(" + parseMeType + "((String) valueObject));");
                } else {
                    sw.println("super." + write.getName() + "((" + read.getReturnType().getCanonicalName() + ") valueObject);");
                }
                sw.outdent();
            }
        }
        sw.println("}");
        sw.outdent();
        sw.println("}");
    }
