    private void introspect(ClassPool classPool) {
        Class<? super C> parent = ivClass.getSuperclass();
        int offset = (parent != null) ? 1 : 0;
        Class<?>[] interfaces = ivClass.getInterfaces();
        ivParents = new ClassInfo<?>[interfaces.length + offset];
        if (parent != null) ivParents[0] = getInstance(parent);
        for (int i = 0; i < interfaces.length; i++) {
            ivParents[i + offset] = getInstance(interfaces[i]);
        }
        try {
            ivConstructor = ivClass.getConstructor();
            ivConstructor.setAccessible(true);
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchMethodException ex) {
        }
        Method[] methods = ivClass.getMethods();
        class Bag {

            Class<?> declaringClass;

            Method readMethod;

            Method writeMethod;

            Class<?> readType;

            Class<?> writeType;
        }
        Map<String, Bag> getSetProps = new HashMap<String, Bag>();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            int mods = method.getModifiers();
            if (Modifier.isStatic(mods) || !Modifier.isPublic(mods)) continue;
            String name = method.getName();
            Class<?> argTypes[] = method.getParameterTypes();
            int argCount = argTypes.length;
            String property = null;
            if (argCount == 0) {
                Class<?> resultType = method.getReturnType();
                final boolean isAGet = name.startsWith("get");
                if (isAGet || ((resultType == boolean.class || resultType == Boolean.class) && name.startsWith("is"))) {
                    property = Introspector.decapitalize(name.substring(isAGet ? 3 : 2));
                    if (LOGGER.isDebugEnabled()) LOGGER.debug("get - " + property + " " + method);
                    Bag bag = getSetProps.get(property);
                    if (bag == null) {
                        bag = new Bag();
                        getSetProps.put(property, bag);
                        bag.declaringClass = method.getDeclaringClass();
                        bag.readType = resultType;
                        bag.readMethod = method;
                    } else if (bag.readMethod == null || bag.declaringClass.isAssignableFrom(method.getDeclaringClass())) {
                        if (bag.declaringClass.isAssignableFrom(method.getDeclaringClass())) {
                            bag.declaringClass = method.getDeclaringClass();
                        }
                        bag.readType = resultType;
                        bag.readMethod = method;
                    }
                } else if (resultType != void.class && ivClass == method.getDeclaringClass()) {
                    property = name + "()";
                    if (LOGGER.isDebugEnabled()) LOGGER.debug(property + " " + method);
                    addReadMethodProperty(classPool, property, resultType, method);
                }
            } else if (argCount == 1) {
                if (name.startsWith("set")) {
                    property = Introspector.decapitalize(name.substring(3));
                    if (LOGGER.isDebugEnabled()) LOGGER.debug("set - " + property + " " + method);
                    Bag bag = getSetProps.get(property);
                    if (bag == null) {
                        bag = new Bag();
                        getSetProps.put(property, bag);
                        bag.declaringClass = method.getDeclaringClass();
                        bag.writeType = argTypes[0];
                        bag.writeMethod = method;
                    } else if (bag.writeMethod == null || bag.declaringClass.isAssignableFrom(method.getDeclaringClass())) {
                        if (bag.declaringClass.isAssignableFrom(method.getDeclaringClass())) {
                            bag.declaringClass = method.getDeclaringClass();
                        }
                        bag.writeType = argTypes[0];
                        bag.writeMethod = method;
                    }
                }
            }
        }
        for (Map.Entry<String, Bag> en : getSetProps.entrySet()) {
            Bag bag = en.getValue();
            if (bag.declaringClass == ivClass) {
                addGetSetProperty(classPool, en.getKey(), bag.readMethod, bag.readType, bag.writeMethod, bag.writeType);
            }
        }
    }
