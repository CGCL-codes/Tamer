    public void callWriteObject(Class type, Object instance, ObjectOutputStream stream) {
        try {
            Method readObjectMethod = getMethod(type, "writeObject", new Class[] { ObjectOutputStream.class }, false);
            readObjectMethod.invoke(instance, new Object[] { stream });
        } catch (IllegalAccessException e) {
            throw new ConversionException("Could not call " + instance.getClass().getName() + ".writeObject()", e);
        } catch (InvocationTargetException e) {
            throw new ConversionException("Could not call " + instance.getClass().getName() + ".writeObject()", e.getTargetException());
        }
    }
