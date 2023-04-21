    private void generateAccessors(SourceWriter sw) {
        sw.println("public String getUUID() {");
        sw.indent();
        sw.println("return uuid;");
        sw.outdent();
        sw.println("}");
        sw.println();
        sw.println("public void setSettableValues(boolean settable) {");
        sw.indent();
        sw.println("propertySettable = settable;");
        sw.outdent();
        sw.println("}");
        sw.println();
        PropertyDescriptor[] props = PropertyUtils.getPropertyDescriptors(superClass);
        for (PropertyDescriptor pd : props) {
            Method write = pd.getWriteMethod();
            Method read = pd.getReadMethod();
            if (write != null && read != null) {
                StringBuffer buff = new StringBuffer();
                Class<?> propType = read.getReturnType();
                buff.append("public void ").append(write.getName()).append("(").append(propType.getCanonicalName()).append(" newVal) {");
                sw.println(buff.toString());
                sw.indent();
                sw.println("if (propertySettable) {");
                sw.indent();
                sw.println("super." + write.getName() + "(newVal);");
                sw.outdent();
                sw.println("} else {");
                sw.indent();
                sw.println("WaveClientHelper.syncData(this, \"" + pd.getName() + "\", String.valueOf(newVal));");
                sw.outdent();
                sw.println("}");
                sw.outdent();
                sw.println("}");
            }
        }
        sw.println();
    }
