        public GenericBeanFieldHandler(GenericBean<B> instance) throws IOException {
            beanInstance = instance;
            readMethods = new ArrayList<Method>();
            writeMethods = new ArrayList<Method>();
            fieldHandlers = new ArrayList<FieldHandler>();
            try {
                final BeanInfo beanInfo = Introspector.getBeanInfo(instance.getClass());
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor descriptor : propertyDescriptors) {
                    final Method readMethod = descriptor.getReadMethod();
                    final Method writeMethod = descriptor.getWriteMethod();
                    if (readMethod != null && writeMethod != null && readMethod.isAnnotationPresent(AttrInfo.class)) {
                        readMethods.add(readMethod);
                        writeMethods.add(writeMethod);
                        final Object field = readMethod.invoke(instance);
                        final FieldHandler fieldHandler = createFieldHandler(field);
                        if (fieldHandler == null) {
                            throw new IOException("Invalid attribute found: " + readMethod.getName());
                        }
                        fieldHandlers.add(fieldHandler);
                    }
                }
            } catch (IntrospectionException e) {
                e.printStackTrace();
                throw new IOException(e.getMessage());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new IOException(e.getMessage());
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                throw new IOException(e.getMessage());
            }
        }
