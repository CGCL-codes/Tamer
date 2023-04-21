    private void buildDynamicMBeanInfo() throws IntrospectionException {
        Constructor[] constructors = this.getClass().getConstructors();
        dConstructors[0] = new MBeanConstructorInfo("LayoutDynamicMBean(): Constructs a LayoutDynamicMBean instance", constructors[0]);
        BeanInfo bi = Introspector.getBeanInfo(layout.getClass());
        PropertyDescriptor[] pd = bi.getPropertyDescriptors();
        int size = pd.length;
        for (int i = 0; i < size; i++) {
            String name = pd[i].getName();
            Method readMethod = pd[i].getReadMethod();
            Method writeMethod = pd[i].getWriteMethod();
            if (readMethod != null) {
                Class returnClass = readMethod.getReturnType();
                if (isSupportedType(returnClass)) {
                    String returnClassName;
                    if (returnClass.isAssignableFrom(Level.class)) {
                        returnClassName = "java.lang.String";
                    } else {
                        returnClassName = returnClass.getName();
                    }
                    dAttributes.add(new MBeanAttributeInfo(name, returnClassName, "Dynamic", true, writeMethod != null, false));
                    dynamicProps.put(name, new MethodUnion(readMethod, writeMethod));
                }
            }
        }
        MBeanParameterInfo[] params = new MBeanParameterInfo[0];
        dOperations[0] = new MBeanOperationInfo("activateOptions", "activateOptions(): add an layout", params, "void", MBeanOperationInfo.ACTION);
    }
