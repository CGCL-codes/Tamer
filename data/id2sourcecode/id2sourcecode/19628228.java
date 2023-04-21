    public static Object newMsg(Object[] _p) {
        FooLib.checkClassArgument(Class.class, _p[0], 0);
        Class c = (Class) _p[0];
        Constructor m = FooLib.getConstructor(c, c.getName(), _p.length - 1);
        Object[] q = new Object[_p.length - 1];
        for (int i = 0; i < q.length; i++) q[i] = _p[i + 1];
        return FooLib.invokeConstructor(m, q);
    }
