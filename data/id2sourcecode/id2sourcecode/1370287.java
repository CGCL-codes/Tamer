    private void createGetMethod(PrintWriter writer, String className, Method readMethod, String propertyName) {
        if ((readMethod != null) && (!"class".equals(propertyName))) {
            String getMethod;
            if (Date.class.isAssignableFrom(readMethod.getReturnType())) {
                getMethod = getDateMethodTemplate;
            } else {
                getMethod = getMethodTemplate;
            }
            getMethod = classPattern.matcher(getMethod).replaceAll(className);
            getMethod = methodPattern.matcher(getMethod).replaceAll(readMethod.getName());
            getMethod = propertyPattern.matcher(getMethod).replaceAll(propertyName);
            getMethod = indentPattern.matcher(getMethod).replaceAll(indent);
            writer.write(getMethod);
        }
    }
