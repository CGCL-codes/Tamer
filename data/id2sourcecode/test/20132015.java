    public JavaMethodProperty(JavaTypeFactory provider, String name, JavaMethod readMethod, JavaMethod writeMethod) {
        if (name == null || (readMethod == null && writeMethod == null)) throw new NullPointerException("Invalid parameters");
        this.name = name;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
        this.type = (readMethod != null ? readMethod.getMember().getReturnType() : writeMethod.getMember().getParameterTypes()[0]);
        this.as3Type = provider.getAs3Type(type);
        this.externalizedProperty = (readMethod != null && readMethod.getMember().isAnnotationPresent(ExternalizedProperty.class));
    }
