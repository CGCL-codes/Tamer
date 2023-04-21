    public Object apply(Context context, Object[] args) {
        if (args.length < 1) {
            throw new BadArgument("Wrong number of arguments to bind()");
        }
        if (args[0] == null) {
            throw new NullArgument("Null first argument to bind()");
        }
        if (!(args[0] instanceof Function)) {
            throw new TypeError("First argument to bind() not a function");
        }
        if (args.length == 1) {
            return args[0];
        }
        Object[] objs = new Object[args.length - 1];
        for (int i = 0; i < objs.length; ++i) {
            objs[i] = args[i + 1];
        }
        return new BoundLastFunction((Function) args[0], objs);
    }
