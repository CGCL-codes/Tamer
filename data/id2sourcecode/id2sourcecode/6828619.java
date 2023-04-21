    public NodeTableModel(ReadableDataNode data, int offset) throws IOException {
        this.data = data;
        recordCache = new ArrayList();
        fetchData(offset);
        Object record = data.newRecord();
        PropertyDescriptor[] propertyDescriptors;
        try {
            propertyDescriptors = Introspector.getBeanInfo(record.getClass()).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new IOException(e.getMessage());
        }
        ArrayList<Method> readMethods = new ArrayList<Method>(propertyDescriptors.length);
        ArrayList<String> readablePropertyNames = new ArrayList<String>(propertyDescriptors.length);
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            final Method readMethod = descriptor.getReadMethod();
            final Method writeMethod = descriptor.getWriteMethod();
            if (readMethod != null && writeMethod != null) {
                readMethods.add(readMethod);
                readablePropertyNames.add(descriptor.getDisplayName());
            }
        }
        methods = readMethods.toArray(new Method[readMethods.size()]);
        propertyNames = readablePropertyNames.toArray(new String[readablePropertyNames.size()]);
    }
