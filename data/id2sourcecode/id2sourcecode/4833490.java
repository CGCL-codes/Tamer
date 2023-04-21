    public BeanParameters(Object bean, Set<String> paramNames) {
        try {
            this.bean = bean;
            BeanInfo info = Introspector.getBeanInfo(bean.getClass(), Object.class);
            PropertyDescriptor[] pds = info.getPropertyDescriptors();
            @SuppressWarnings("unchecked") List<Pair<String, Class>> pairs = new ArrayList<Pair<String, Class>>();
            for (int i = 0; i < pds.length; i++) {
                PropertyDescriptor pd = pds[i];
                String name = pd.getName();
                if (paramNames == null || paramNames.contains(name)) {
                    FastClass fastClass = FastClass.create(bean.getClass());
                    FastMethod readMethod = fastClass.getMethod(pd.getReadMethod());
                    FastMethod writeMethod = pd.getWriteMethod() == null ? null : fastClass.getMethod(pd.getWriteMethod());
                    parameters.put(name, new ObjectParameter(readMethod, writeMethod));
                    pairs.add(new Pair<String, Class>(name, pd.getPropertyType()));
                }
            }
            schema = new DefaultSchema(pairs.toArray(new Pair[pairs.size()]));
        } catch (IntrospectionException e) {
            msgCenter.error("Unable to create feature attributes from agent", e);
        }
        pcs = new PropertyChangeSupport(this);
    }
