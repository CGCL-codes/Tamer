    public JMethodProperty(String name, JMethod readMethod, JMethod writeMethod) {
        if (name == null || (readMethod == null && writeMethod == null)) throw new NullPointerException("Invalid parameters");
        this.name = name;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
        this.type = (readMethod != null ? readMethod.getMember().getReturnType() : writeMethod.getMember().getParameterTypes()[0]);
    }
