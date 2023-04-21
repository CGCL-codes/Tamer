    public static <T extends VagueObject> void checkVagueType(Class<T> type) throws IllegalArgumentException, IntrospectionException {
        BeanInfo info = Introspector.getBeanInfo(type);
        Vector<Method> methods = new Vector<Method>(Arrays.asList(type.getMethods()));
        List<PropertyDescriptor> props = Arrays.asList(info.getPropertyDescriptors());
        for (PropertyDescriptor desc : props) {
            Class propType = desc.getPropertyType();
            if (!Serializable.class.isAssignableFrom(propType) && !propType.isPrimitive()) {
                throw new IllegalArgumentException("Property " + desc.getName() + " must be of a primitive or " + Serializable.class.getName() + " type.");
            } else if (!VagueObject.class.isAssignableFrom(propType)) {
                Method r = desc.getReadMethod();
                Method w = desc.getWriteMethod();
                if (r == null || w == null) {
                    throw new IllegalArgumentException("Property " + desc.getName() + " must be read/write (i.e. both getX and setX)");
                } else {
                    methods.remove(r);
                    methods.remove(w);
                }
            } else {
                if (desc.getWriteMethod() != null) {
                    throw new IllegalArgumentException("Property " + desc.getName() + " must be read-only (i.e. getX, but no setX)");
                } else {
                    checkVagueType(propType);
                }
                Method r = desc.getReadMethod();
                methods.remove(r);
            }
            methods.remove(desc);
        }
        if (!methods.isEmpty()) {
            Method m = methods.iterator().next();
            throw new IllegalArgumentException("Illegal method: " + m.getName() + ". All methods must be read/write (getX and setX) of a sub-type of " + Serializable.class.getName() + " or read-only of sub-type " + VagueObject.class.getName());
        }
    }
