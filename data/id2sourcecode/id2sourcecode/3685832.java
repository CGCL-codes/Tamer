    private static void writeDescriptorClass(EasyTreeLogger logger, SourceWriter writer, GeneratorContext context, JClassType beanType, JProperty property) throws UnableToCompleteException {
        String boxedTypeName = boxedTypeName(property.getType());
        String beanTypeName = beanType.getQualifiedSourceName();
        String propertyTypeId = property.getType().toString();
        logger = logger.branchDebug("Writing property descriptor for '" + property.getName() + "'");
        writer.println("private static class " + property.getName() + "_Descriptor extends AbstractPropertyDescriptor<" + beanTypeName + ", " + boxedTypeName + "> {");
        writer.println("    public " + property.getName() + "_Descriptor(BeanInfo beanInfo) {");
        writer.println("        super(\"" + property.getName() + "\", \"" + propertyTypeId + "\", beanInfo);");
        writer.println("        buildPropertyType();");
        writer.println("    }");
        writer.println("public boolean isReadable() {");
        writer.println("    return " + (property.getGetter() != null) + ";");
        writer.println("}");
        writer.println("public boolean isWritable() {");
        writer.println("    return " + (property.getSetter() != null) + ";");
        writer.println("}");
        logger.debug("writing setter method for '" + property.getName() + "' ('" + boxedTypeName + "')");
        writer.println("    public void setValue(" + beanTypeName + " bean, " + boxedTypeName + " value) {");
        if (property.getSetter() != null) {
            if (property.getType().isPrimitive() != null) {
                writer.println("        if (value == null) {");
                writer.println("            throw new TypeMismatchException(" + property.getType().getQualifiedSourceName() + ".class, null);");
                writer.println("        }");
            }
            writer.println("        bean." + property.getSetter().getName() + "(value);");
        } else {
            writer.println("        throw new UnsupportedOperationException(\"Property '" + property.getName() + "' of bean '" + beanType.getQualifiedSourceName() + "' is not writable (it has no setter\");");
        }
        writer.println("    }");
        logger.debug("writing getter method for '" + property.getName() + "' ('" + boxedTypeName + "')");
        writer.println("    public " + boxedTypeName + " getValue(" + beanTypeName + " bean) {");
        if (property.getGetter() != null) {
            writer.println("        return bean." + property.getGetter().getName() + "();");
        } else {
            writer.println("        throw new UnsupportedOperationException(\"Property '" + property.getName() + "' of bean '" + beanType.getQualifiedSourceName() + "' is not readable (it has no getter\");");
        }
        writer.println("    }");
        String displayName = resolveDisplayName(property);
        if (displayName != null) {
            writer.println("    public String getDisplayName() {");
            writer.println("        return \"" + displayName + "\";");
            writer.println("    }");
        }
        String description = resolveDescription(property);
        if (description != null) {
            writer.println("    public String getDescription() {");
            writer.println("        return \"" + description + "\";");
            writer.println("    }");
        }
        writer.println("    private static Type<" + boxedTypeName + "> buildPropertyType() {");
        String typeVarName = writeTypeDesclaration(logger, writer, context, property.getType());
        writer.println("        return " + typeVarName + ";");
        writer.println("    }");
        writer.indent();
        writeAttributes(logger, writer, context, beanType, property);
        writer.outdent();
        writer.println("}");
    }
