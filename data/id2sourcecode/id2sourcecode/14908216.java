    public Object call(Object... args) {
        Function f = (Function) args[0];
        AbstractList l = (AbstractList) args[args.length - 1];
        Object[] a = new Object[l.size() + args.length - 2];
        for (int i = 0; i < args.length - 2; i++) {
            a[i] = args[i + 1];
        }
        for (int i = args.length - 2; i < a.length; i++) {
            a[i] = l.get(i);
        }
        return f.call(a);
    }
