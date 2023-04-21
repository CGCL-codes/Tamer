    public EventTool(String[] args) throws JiapiException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String globalInclude = System.getProperty("include");
        String globalExclude = System.getProperty("exclude");
        String feInclude = System.getProperty("field:include");
        String feExclude = System.getProperty("field:exclude");
        String meInclude = System.getProperty("method:include");
        String meExclude = System.getProperty("method:exclude");
        InstrumentationContext ctx = new InstrumentationContext();
        InstrumentationDescriptor fid = new InstrumentationDescriptor();
        InstrumentationDescriptor mid = new InstrumentationDescriptor();
        if (globalInclude != null) {
            StringTokenizer st = new StringTokenizer(globalInclude, ",");
            while (st.hasMoreTokens()) {
                String rule = st.nextToken();
                fid.addInclusionRule(rule);
                mid.addInclusionRule(rule);
            }
        }
        if (globalExclude != null) {
            StringTokenizer st = new StringTokenizer(globalExclude, ",");
            while (st.hasMoreTokens()) {
                String rule = st.nextToken();
                fid.addExclusionRule(rule);
                mid.addExclusionRule(rule);
            }
        }
        if (feInclude != null) {
            StringTokenizer st = new StringTokenizer(feInclude, ",");
            while (st.hasMoreTokens()) {
                String rule = st.nextToken();
                fid.addInclusionRule(rule);
            }
        }
        if (feExclude != null) {
            StringTokenizer st = new StringTokenizer(feExclude, ",");
            while (st.hasMoreTokens()) {
                String rule = st.nextToken();
                fid.addExclusionRule(rule);
            }
        }
        if (meInclude != null) {
            StringTokenizer st = new StringTokenizer(meInclude, ",");
            while (st.hasMoreTokens()) {
                String rule = st.nextToken();
                mid.addInclusionRule(rule);
            }
        }
        if (meExclude != null) {
            StringTokenizer st = new StringTokenizer(meExclude, ",");
            while (st.hasMoreTokens()) {
                String rule = st.nextToken();
                mid.addExclusionRule(rule);
            }
        }
        FieldEventProducer fieldEventProducer = new FieldEventProducer(fid);
        fieldEventProducer.addFieldListener(this);
        MethodEventProducer methodEventProducer = new MethodEventProducer(mid);
        methodEventProducer.addMethodListener(this);
        ctx.addInstrumentationDescriptor(fid);
        ctx.addInstrumentationDescriptor(mid);
        Class<?> c = InstrumentingClassLoader.createClassLoader(ctx).loadClass(args[0]);
        Method method = c.getMethod("main", new Class[] { String[].class });
        String[] __args = new String[args.length - 1];
        for (int i = 0; i < __args.length; i++) {
            __args[i] = args[i + 1];
        }
        method.invoke(c, new Object[] { __args });
    }
