    public static Object newInstance(Class clazz, Object... args) {
        Class[] argTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }
        Constructor ctor = getConstructor(clazz, argTypes);
        return instantiate(ctor, args);
    }
