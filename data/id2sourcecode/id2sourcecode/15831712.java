    protected void init(Map<Class<?>, JClass> classJClassMap, Map<Class<?>, File> classFilesMap) {
        if (type.getSuperclass() != null && type.getSuperclass() != Object.class) superclass = forType(type.getSuperclass(), classFilesMap, classJClassMap);
        interfaces = new ArrayList<JClass>();
        for (Class<?> interfaze : type.getInterfaces()) {
            if (classFilesMap.containsKey(interfaze)) interfaces.add(forType(interfaze, classFilesMap, classJClassMap));
        }
        interfaces = Collections.unmodifiableList(interfaces);
        Map<String, JProperty> propertyMap = new HashMap<String, JProperty>();
        try {
            BeanInfo info = Introspector.getBeanInfo(type);
            PropertyDescriptor[] propertyDescriptors = info.getPropertyDescriptors();
            if (type.isInterface()) {
                if (propertyDescriptors != null) {
                    for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                        String name = propertyDescriptor.getName();
                        JMethod readMethod = null;
                        JMethod writeMethod = null;
                        Method method = propertyDescriptor.getReadMethod();
                        if (method != null) readMethod = new JMethod(method, MethodType.GETTER);
                        method = propertyDescriptor.getWriteMethod();
                        if (method != null) writeMethod = new JMethod(method, MethodType.SETTER);
                        if (readMethod != null || writeMethod != null) propertyMap.put(name, new JMethodProperty(name, readMethod, writeMethod));
                    }
                }
            } else {
                for (Field field : type.getDeclaredFields()) {
                    if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())) {
                        String name = field.getName();
                        JMethod readMethod = null;
                        JMethod writeMethod = null;
                        if (propertyDescriptors != null) {
                            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                                if (name.equals(propertyDescriptor.getName())) {
                                    if (propertyDescriptor.getReadMethod() != null) readMethod = new JMethod(propertyDescriptor.getReadMethod(), MethodType.GETTER);
                                    if (propertyDescriptor.getWriteMethod() != null) writeMethod = new JMethod(propertyDescriptor.getWriteMethod(), MethodType.SETTER);
                                    break;
                                }
                            }
                        }
                        JFieldProperty property = new JFieldProperty(field, readMethod, writeMethod);
                        propertyMap.put(name, property);
                        if (property.isIdentifier()) {
                            if (identifiers == null) identifiers = new ArrayList<JFieldProperty>();
                            identifiers.add(property);
                        }
                    }
                }
                if (identifiers != null) identifiers = Collections.unmodifiableList(identifiers);
                if (type.isAnnotationPresent(IdClass.class)) idClass = forType(type.getAnnotation(IdClass.class).value(), classFilesMap, classJClassMap);
            }
        } catch (Exception e) {
            throw new GenerationException("Could not introspect properties for: " + type, e);
        }
        properties = new ArrayList<JProperty>(propertyMap.values());
        Collections.sort(properties);
        properties = Collections.unmodifiableList(properties);
        if (!type.isInterface()) {
            Map<String, JProperty> iPropertyMap = new HashMap<String, JProperty>();
            for (JClass interfaze : interfaces) {
                for (JProperty property : interfaze.getProperties()) {
                    String name = property.getName();
                    if (!iPropertyMap.containsKey(name) && !propertyMap.containsKey(name)) iPropertyMap.put(name, property);
                }
            }
            interfaceProperties = new ArrayList<JProperty>(iPropertyMap.values());
            Collections.sort(interfaceProperties);
            interfaceProperties = Collections.unmodifiableList(interfaceProperties);
        }
        imports = new HashSet<Class<?>>();
        if (superclass != null && superclass.getPackage() != type.getPackage()) imports.add(superclass.getType());
        if (idClass != null && idClass.getPackage() != type.getPackage()) imports.add(idClass.getType());
        for (JClass interfaze : interfaces) {
            if (interfaze.getPackage() != type.getPackage()) imports.add(interfaze.getType());
        }
        for (JProperty property : properties) {
            if (property.getType().getPackage() != type.getPackage()) imports.add(property.getType());
        }
        imports = Collections.unmodifiableSet(imports);
    }
