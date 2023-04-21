    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Map toMapData(final Table table, final Object object) {
        Map data = null;
        if (object instanceof Map) {
            data = (Map) object;
        } else {
            boolean bEntity = false;
            if (object instanceof IEntityBeanAware) {
                final IEntityBeanAware entityBeanAware = (IEntityBeanAware) object;
                final Map<String, Column> columns = entityBeanAware.getTableColumnDefinition();
                if (columns != null && columns.size() > 0) {
                    bEntity = true;
                    data = new LinkedHashMap(columns.size());
                    for (final Map.Entry<String, Column> column : columns.entrySet()) {
                        final String propertyName = column.getKey();
                        Object vObject = BeanUtils.getProperty(object, propertyName);
                        if (vObject == null && ID.class.isAssignableFrom(BeanUtils.getPropertyType(object, propertyName))) {
                            vObject = table.getNullId();
                        }
                        data.put(column.getValue().getColumnName(), vObject);
                    }
                }
            }
            if (!bEntity) {
                data = BeanUtils.toMap(object, true, new BeanInvoke() {

                    @Override
                    public Object invoke(final Object bean, final Method readMethod, final Method writeMethod) {
                        try {
                            final Object vObject = readMethod.invoke(bean);
                            if (ID.class.isAssignableFrom(readMethod.getReturnType()) && vObject == null) {
                                return table.getNullId();
                            } else {
                                return vObject;
                            }
                        } catch (final Exception e) {
                            throw BeansOpeException.wrapException(e);
                        }
                    }
                });
            }
        }
        return data;
    }
