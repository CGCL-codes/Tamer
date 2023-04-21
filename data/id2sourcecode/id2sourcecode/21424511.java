    public static Map<String, FieldDescription> analyzeEntity(Class<?> entityType) throws SecurityException {
        Map<String, FieldDescription> descriptions = new HashMap<String, FieldDescription>();
        PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(entityType);
        for (PropertyDescriptor desc : descriptors) {
            try {
                Field field = entityType.getDeclaredField(desc.getName());
                DataAccessMatcher dam = DataAccessMatcher.getInstance();
                Class<?> fieldType = field.getType();
                Class<? extends DataSourceField> dsf = dam.matchField(fieldType);
                String recordAccessor = dam.matchRecordMethod(fieldType);
                if (dsf != null) {
                    String name = desc.getName();
                    String readMethod = desc.getReadMethod().getName();
                    String writeMethod = desc.getWriteMethod().getName();
                    Class<? extends Validator> validator = dam.matchValidator(fieldType);
                    descriptions.put(name, new FieldDescription(name, readMethod, writeMethod, dsf, recordAccessor, validator, true));
                }
            } catch (NoSuchFieldException e) {
            }
        }
        return descriptions;
    }
