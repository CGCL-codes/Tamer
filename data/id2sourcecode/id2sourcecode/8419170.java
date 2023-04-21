    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        try {
            PropertyDescriptor[] propertyDescs = Introspector.getBeanInfo(oldInstance.getClass()).getPropertyDescriptors();
            for (int i = 0; i < propertyDescs.length; i++) {
                Method readMethod = propertyDescs[i].getReadMethod();
                Method writeMethod = propertyDescs[i].getWriteMethod();
                if (readMethod != null && writeMethod != null) {
                    Object oldValue = readMethod.invoke(oldInstance, null);
                    if (oldValue != null) out.writeStatement(new Statement(oldInstance, writeMethod.getName(), new Object[] { oldValue }));
                }
            }
        } catch (IntrospectionException ie) {
            out.getExceptionListener().exceptionThrown(ie);
        } catch (IllegalAccessException iae) {
            out.getExceptionListener().exceptionThrown(iae);
        } catch (InvocationTargetException ite) {
            out.getExceptionListener().exceptionThrown(ite);
        }
    }
