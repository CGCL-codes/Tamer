    private static PropertyAccessor[] createPropertyAccessors(final ConfigurableListableBeanFactory beanFactory, final String beanName, final Class<?> beanClass) {
        final GenericBeanDefinition beanDefinition = (GenericBeanDefinition) beanFactory.getBeanDefinition(beanName);
        final Set<String> springPropertyNames = new HashSet<String>();
        for (PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValues()) {
            springPropertyNames.add(propertyValue.getName());
        }
        final List<PropertyAccessor> accessors = new ArrayList<PropertyAccessor>();
        for (Field field : Reflection.getFields(beanClass)) {
            if (!Modifier.isStatic(field.getModifiers())) {
                final boolean isSpringProperty = springPropertyNames.contains(field.getName());
                if (field.getAnnotation(Context.class) != null) {
                    if (isSpringProperty) {
                        throw new NanoConfigurationException(field, "spring controlled property cannot be a context variable");
                    }
                    Method readMethod = Reflection.findReadMethod(beanClass, field, true);
                    Method writeMethod = Reflection.findWriteMethod(beanClass, field, true);
                    accessors.add(new PropertyAccessor(readMethod, writeMethod));
                } else {
                    if (!isSpringProperty && !Modifier.isTransient(field.getModifiers())) {
                        throw new NanoConfigurationException(field, "missing @Context annotation");
                    }
                }
            }
        }
        return accessors.toArray(new PropertyAccessor[accessors.size()]);
    }
