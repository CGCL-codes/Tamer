    private DynamicObject2D buildConstruction(Class<?> objectClass, ArrayList<Class<?>> argClasses, ArrayList<Object> args) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Object[] tabArgs = new Object[args.size()];
        Class<?>[] tabClasses = new Class<?>[args.size()];
        for (int i = 0; i < args.size(); i++) {
            tabArgs[i] = args.get(i);
            tabClasses[i] = argClasses.get(i);
        }
        if (AngleWrapper2D.class.isAssignableFrom(objectClass)) {
            logger.trace("Angle wrapper found");
            double angle = (Double) (tabArgs[0]);
            tabArgs[0] = new AngleMeasure2D(angle, AngleUnit.DEGREE);
            tabClasses[0] = AngleMeasure2D.class;
        }
        Constructor<?>[] constructors = objectClass.getConstructors();
        Constructor<?> constructor = null;
        for (int i = 0; i < constructors.length; i++) {
            Class<?>[] consClasses = constructors[i].getParameterTypes();
            if (consClasses.length != tabClasses.length) continue;
            int j;
            for (j = 0; j < consClasses.length; j++) if (!consClasses[j].isAssignableFrom(tabClasses[j])) break;
            if (j == consClasses.length) {
                constructor = constructors[i];
                break;
            }
        }
        if (constructor == null) {
            logger.error("Could not find constructor for object of class [" + objectClass.getSimpleName() + "].");
            return null;
        }
        Object obj = constructor.newInstance(tabArgs);
        DynamicObject2D object;
        if (obj instanceof Shape2D) object = new ShapeWrapper2D((Shape2D) obj); else object = (DynamicObject2D) obj;
        return object;
    }
