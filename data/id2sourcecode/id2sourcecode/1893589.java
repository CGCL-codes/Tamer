    public BeanBusManager createBeanBus(URL url) {
        try {
            InputStream is = url.openStream();
            BeanBusManager beanBusManager = createBeanBus(is);
            is.close();
            return beanBusManager;
        } catch (IOException e) {
            throw new BeanBusCreateException("BeanBus can't create", e);
        }
    }
