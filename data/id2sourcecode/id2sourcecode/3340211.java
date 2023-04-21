    @Override
    @SuppressWarnings("unchecked")
    public T newInstance(BeanDesc<T> beanDesc, Object[] args) {
        Class<?> originalClass = beanDesc.getComponentClass();
        Object thisInstance;
        if (originalClass.isInterface()) {
            thisInstance = Proxy.newProxyInstance(ClassLoaderUtil.getClassLoader(originalClass), new Class[] { originalClass }, noOpInvocationHandler);
        } else {
            ConstructorDesc<T> cd = beanDesc.getConstructorDesc();
            Constructor<T> c = cd.getSuitableConstructor(args);
            if (c == null) {
                throw new ConstructorNotFoundRuntimeException(beanDesc.getConcreteClass().getName());
            }
            thisInstance = ConstructorUtil.newInstance(c, args);
        }
        Class<?> proxyClass = beanDesc.getEnhancedComponentClass();
        ProxyInvocationHandler proxyHandler = new ProxyInvocationHandler(proxyInterceptorMap, thisInstance);
        Constructor<?> proxyConstructor = ConstructorUtil.getConstructor(proxyClass, new Class[] { InvocationHandler.class });
        return (T) ConstructorUtil.newInstance(proxyConstructor, new Object[] { proxyHandler });
    }
