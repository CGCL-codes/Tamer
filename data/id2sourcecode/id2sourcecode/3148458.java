    protected void init(Class<T> cls, Key... keys) {
        readDataSource = Execution.getDataSourceName(cls.getName(), true);
        writeDataSource = Execution.getDataSourceName(cls.getName(), false);
        if (readDataSource == null) {
            readDataSource = writeDataSource;
        }
        if (writeDataSource == null) {
            writeDataSource = readDataSource;
        }
        Properties props = new Properties();
        try {
            InputStream is = DaseinSequencer.class.getResourceAsStream(DaseinSequencer.PROPERTIES);
            if (is != null) {
                props.load(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        database = props.getProperty("dasein.persist.handlersocket.database");
        handlerSocketHost = props.getProperty("dasein.persist.handlersocket.host");
        port = Integer.valueOf(props.getProperty("dasein.persist.handlersocket.port"));
        poolSize = Integer.valueOf(props.getProperty("dasein.persist.handlersocket.poolSize"));
        List<String> targetColumns = new ArrayList<String>();
        Class<?> clazz = cls;
        while (clazz != null && !clazz.equals(Object.class)) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                int m = f.getModifiers();
                if (Modifier.isTransient(m) || Modifier.isStatic(m)) {
                    continue;
                }
                if (f.getType().getName().equals(Collection.class.getName())) {
                    continue;
                }
                if (!f.getType().getName().equals(Translator.class.getName())) {
                    targetColumns.add(f.getName());
                    types.put(f.getName(), f.getType());
                }
            }
            clazz = clazz.getSuperclass();
        }
        columns = new String[targetColumns.size()];
        databaseColumns = new String[targetColumns.size()];
        for (int i = 0; i < targetColumns.size(); i++) {
            columns[i] = targetColumns.get(i);
            databaseColumns[i] = getSqlName(targetColumns.get(i));
        }
    }
