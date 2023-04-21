    private static final Object[] bind(final Class clazz, final String beanProperty) {
        Object[] result = new Object[2];
        byte[] array = beanProperty.toLowerCase().getBytes();
        array[0] = (byte) Character.toUpperCase((char) array[0]);
        String nowPropertyName = new String(array);
        String names[] = { ("set" + nowPropertyName).intern(), ("get" + nowPropertyName).intern(), ("is" + nowPropertyName).intern(), ("write" + nowPropertyName).intern(), ("read" + nowPropertyName).intern() };
        java.lang.reflect.Method getter = null;
        java.lang.reflect.Method setter = null;
        java.lang.reflect.Method methods[] = clazz.getMethods();
        for (int i = 0; i < methods.length; i++) {
            java.lang.reflect.Method method = methods[i];
            if (!java.lang.reflect.Modifier.isPublic(method.getModifiers())) {
                continue;
            }
            String methodName = method.getName().intern();
            for (int j = 0; j < names.length; j++) {
                String name = names[j];
                if (!name.equals(methodName)) {
                    continue;
                }
                if (methodName.startsWith("set") || methodName.startsWith("read")) {
                    setter = method;
                } else {
                    getter = method;
                }
            }
        }
        result[0] = getter;
        result[1] = setter;
        return result;
    }
