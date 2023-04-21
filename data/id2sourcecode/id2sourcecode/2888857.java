    private void createAttributes(List<FeatureAttributeAdapter> adapters) {
        try {
            BeanInfo info = Introspector.getBeanInfo(agentObj.getClass(), Object.class);
            PropertyDescriptor[] pds = info.getPropertyDescriptors();
            for (int i = 0; i < pds.length; i++) {
                PropertyDescriptor pd = pds[i];
                String featureName = pd.getName();
                FastClass fastClass = FastClass.create(agentObj.getClass());
                if (pd.getReadMethod() != null) {
                    FastMethod readMethod = fastClass.getMethod(pd.getReadMethod());
                    FastMethod writeMethod = pd.getWriteMethod() == null ? null : fastClass.getMethod(pd.getWriteMethod());
                    attributeMap.put(featureName, new ObjectFeatureAgentAttribute(featureName, readMethod, writeMethod));
                }
            }
            for (FeatureAttributeAdapter adapter : adapters) {
                attributeMap.put(adapter.getAttributeName(), adapter);
            }
        } catch (IntrospectionException e) {
            msgCenter.error("Unable to create feature attributes from agent", e);
        }
    }
