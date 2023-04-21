    public static Object newMsg(Object[] _p) {
        FooLib.checkClassArgument(Class.class, _p[0], 0);
        Class c = (Class) _p[0];
        if (c.isArray()) {
            int l = _p.length - 1;
            int[] d = new int[l];
            for (int i = 0; i < l; i++) {
                c = c.getComponentType();
                d[i] = FooLib.toInteger(_p[i + 1]).intValue();
            }
            return Array.newInstance(c, d);
        }
        Constructor m = FooLib.getConstructor(c, c.getName(), _p.length - 1);
        if (m == null) {
            StringBuffer s = new StringBuffer(c.getName() + "(");
            for (int j = 0; j < _p.length; j++) {
                if (j > 0) s.append(',');
                s.append(FooLib.getClassName(_p[j]));
            }
            s.append(')');
            throw new RuntimeException("constructor not found:" + s);
        }
        Object[] q = new Object[_p.length - 1];
        for (int i = 0; i < q.length; i++) q[i] = _p[i + 1];
        return FooLib.invokeConstructor(m, q);
    }
