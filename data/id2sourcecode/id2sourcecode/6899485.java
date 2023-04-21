    @Override
    public PropertyAnnotations scanPropertyAnnotations(Object bean, IPropertyPath propertyPath) {
        final Class<?> metaClass = bean.getClass();
        IPropertyChain2 propertyChain = getPropertyChain(metaClass, propertyPath);
        Object lastBean = propertyChain.getLastBean(bean);
        if (lastBean != null) {
            IPropertyElement[] elements = propertyPath.getPathElements();
            IPropertyElement lastElement = elements[elements.length - 1];
            PropertyDescriptor descriptor = getPropertyDescriptor2(lastBean.getClass(), lastElement);
            if (descriptor != null) {
                Method readMethod = descriptor.getReadMethod();
                Method writeMethod = descriptor.getWriteMethod();
                Field field = null;
                if (readMethod != null) {
                    try {
                        Class<?> modelType = readMethod.getDeclaringClass();
                        String propertyName = lastElement.getIdentifier();
                        Field[] fields = modelType.getFields();
                        field = modelType.getField(propertyName);
                    } catch (Throwable ex) {
                    }
                }
                return new PropertyAnnotations(readMethod.getAnnotations(), writeMethod.getAnnotations(), field != null ? field.getAnnotations() : null);
            }
        }
        return null;
    }
