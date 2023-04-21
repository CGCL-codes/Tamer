    protected void addMethods(MethodHandler handler, Method template, Method[] methods) {
        int i;
        int n;
        Method method;
        boolean equal;
        String name;
        for (i = 0; i < methods.length; i++) {
            method = methods[i];
            if (template.equals(method)) continue;
            if (!template.getReturnType().equals(method.getReturnType())) continue;
            if (template.getParameterTypes().length != method.getParameterTypes().length) continue;
            equal = true;
            for (n = 0; n < template.getParameterTypes().length; n++) {
                if (!template.getParameterTypes()[n].equals(method.getParameterTypes()[n])) {
                    equal = false;
                    break;
                }
            }
            if (equal) {
                name = method.getName();
                name = name.replaceAll("read|write", "");
                name = name.substring(0, 1).toLowerCase() + name.substring(1);
                handler.add(name, method);
            }
        }
    }
