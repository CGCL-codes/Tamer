    void initialize(Logger pLogger) throws XelException {
        try {
            mBeanInfo = Introspector.getBeanInfo(mBeanClass);
            mPropertyByName = new HashMap();
            mIndexedPropertyByName = new HashMap();
            PropertyDescriptor[] pds = mBeanInfo.getPropertyDescriptors();
            for (int i = 0; pds != null && i < pds.length; i++) {
                PropertyDescriptor pd = pds[i];
                if (pd instanceof IndexedPropertyDescriptor) {
                    IndexedPropertyDescriptor ipd = (IndexedPropertyDescriptor) pd;
                    Method readMethod = getPublicMethod(ipd.getIndexedReadMethod());
                    Method writeMethod = getPublicMethod(ipd.getIndexedWriteMethod());
                    BeanInfoIndexedProperty property = new BeanInfoIndexedProperty(readMethod, writeMethod, ipd);
                    mIndexedPropertyByName.put(ipd.getName(), property);
                }
                Method readMethod = getPublicMethod(pd.getReadMethod());
                Method writeMethod = getPublicMethod(pd.getWriteMethod());
                BeanInfoProperty property = new BeanInfoProperty(readMethod, writeMethod, pd);
                mPropertyByName.put(pd.getName(), property);
            }
            mEventSetByName = new HashMap();
            EventSetDescriptor[] esds = mBeanInfo.getEventSetDescriptors();
            for (int i = 0; esds != null && i < esds.length; i++) {
                EventSetDescriptor esd = esds[i];
                mEventSetByName.put(esd.getName(), esd);
            }
        } catch (IntrospectionException exc) {
            if (pLogger.isLoggingWarning()) {
                pLogger.logWarning(Constants.EXCEPTION_GETTING_BEANINFO, exc, mBeanClass.getName());
            }
        }
    }
