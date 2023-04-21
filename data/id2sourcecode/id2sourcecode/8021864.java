    private void doTest(Object bean, PropertyInformation property, Configuration configuration) throws BeanTestException {
        log.debug("doTest: entering with bean=[" + bean + "], property=[" + property + "], configuration=[" + configuration + "].");
        validationHelper.ensureExists("bean", "test", bean);
        validationHelper.ensureExists("property", "test", property);
        Method writeMethod = property.getWriteMethod();
        Class<?>[] parameterTypes = writeMethod.getParameterTypes();
        if (parameterTypes.length != 1) {
            String message = "writeMethod " + property.getName() + "." + writeMethod.getName() + " must accept only 1 paramter, but accepts [" + parameterTypes.length + "] parameters. It is not a JavaBean property.";
            log.debug("doTest: " + message + " Throw BeanTestException.");
            throw new BeanTestException(message);
        }
        Class<?> parameterType = parameterTypes[0];
        Factory<?> factory = getFactory(property.getName(), parameterType, configuration);
        Object testValue = factory.create();
        doInvokeMethod(bean, writeMethod, testValue);
        Method readMethod = property.getReadMethod();
        Class<?> returnType = readMethod.getReturnType();
        if (!returnType.isAssignableFrom(parameterType)) {
            String message = "readMethod " + property.getName() + "." + readMethod.getName() + " must return same type as writeMethod " + property.getName() + "." + writeMethod.getName() + " accepts. It is not a JavaBean property.";
            log.debug("doTest: " + message + " Throw BeanTestException.");
            throw new BeanTestException(message);
        }
        Object readMethodOutput = doInvokeMethod(bean, readMethod);
        doCompare(testValue, readMethodOutput);
        log.debug("doTest: exiting.");
    }
