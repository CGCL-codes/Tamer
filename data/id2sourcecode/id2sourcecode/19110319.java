    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Map toMap(final Object bean, final boolean readAndWrite, final BeanInvoke invoke) {
        final Map map = invoke.createMap();
        if (bean instanceof Map) {
            map.putAll((Map) bean);
        } else if (bean != null) {
            for (final PropertyDescriptor descriptor : getPropertyDescriptors(bean.getClass())) {
                Method readMethod, writeMethod = null;
                if ((readMethod = getReadMethod(descriptor)) != null && (!readAndWrite || (readAndWrite && (writeMethod = getWriteMethod(descriptor)) != null))) {
                    try {
                        final Object o = invoke.invoke(bean, readMethod, writeMethod);
                        if (o != null) {
                            map.put(descriptor.getName(), o);
                        }
                    } catch (final Exception e) {
                        throw BeansOpeException.wrapException(e);
                    }
                }
            }
        }
        return map;
    }
