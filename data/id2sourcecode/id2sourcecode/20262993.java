    public static <T extends OIDJavaBean> T cloneBeanDeep(final T bean) {
        if (bean == null) {
            return null;
        }
        try {
            final T result = BeanUtil.cloneBean(bean);
            final BeanInfo beanInfo = Introspector.getBeanInfo(result.getClass());
            final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (final PropertyDescriptor descriptor : propertyDescriptors) {
                final Class<?> propertyType = descriptor.getPropertyType();
                if (IModel.class.isAssignableFrom(propertyType)) {
                    final Method readMethod = descriptor.getReadMethod();
                    final Method writeMethod = descriptor.getWriteMethod();
                    if (!readMethod.isAccessible()) {
                        readMethod.setAccessible(true);
                    }
                    if (!writeMethod.isAccessible()) {
                        writeMethod.setAccessible(true);
                    }
                    if (readMethod != null && writeMethod != null && readMethod.isAccessible() && writeMethod.isAccessible()) {
                        final Object child0 = readMethod.invoke(result, new Object[0]);
                        final OIDJavaBean child = cloneBeanDeep((OIDJavaBean) child0);
                        writeMethod.invoke(result, child);
                    }
                } else if (List.class.isAssignableFrom(propertyType)) {
                    final Method readMethod = descriptor.getReadMethod();
                    final Method writeMethod = descriptor.getWriteMethod();
                    if (!readMethod.isAccessible()) {
                        readMethod.setAccessible(true);
                    }
                    if (!writeMethod.isAccessible()) {
                        writeMethod.setAccessible(true);
                    }
                    if (readMethod != null && writeMethod != null && readMethod.isAccessible() && writeMethod.isAccessible()) {
                        final List child0 = (List) readMethod.invoke(result, new Object[0]);
                        final List clonedList = cloneListDeep(child0);
                        writeMethod.invoke(result, clonedList);
                    }
                }
            }
            return result;
        } catch (final Throwable ex) {
            final String error = "OkAuthServiceSimulator: ERROR: can't clone bean '" + bean.getClass().getName() + "': " + ex.getMessage();
            log.error(error, ex);
            return null;
        }
    }
