    private void simplePropertyTest(Object bean, PropertyDescriptor descriptor, Method readMethod, Method writeMethod) throws Exception {
        Class propertyType = descriptor.getPropertyType();
        InstanceFactory instances = (InstanceFactory) factoryMap.get(propertyType);
        if (instances == null) {
            instances = new NestedBeanInstanceFactory(propertyType);
        }
        Object obj;
        Object invoke;
        for (Iterator it = instances.iterator(); it.hasNext(); Assert.assertEquals("Property:" + descriptor.getDisplayName(), obj, invoke)) {
            obj = it.next();
            try {
                writeMethod.invoke(bean, new Object[] { obj });
                invoke = readMethod.invoke(bean, new Object[0]);
            } catch (Exception ex) {
                throw new BeanRunnerInvocationException(bean, descriptor, obj, ex);
            }
        }
    }
