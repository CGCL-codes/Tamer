    private void createAttributes(Map<String, String> agentFeatureMap) {
        try {
            BeanInfo info = Introspector.getBeanInfo(agentObj.getClass(), Object.class);
            PropertyDescriptor[] pds = info.getPropertyDescriptors();
            for (int i = 0; i < pds.length; i++) {
                PropertyDescriptor pd = pds[i];
                String featureName = getFeatureName(agentFeatureMap, pd.getName());
                if (featureName != null) {
                    FastClass fastClass = FastClass.create(agentObj.getClass());
                    FastMethod readMethod = fastClass.getMethod(pd.getReadMethod());
                    FastMethod writeMethod = pd.getWriteMethod() == null ? null : fastClass.getMethod(pd.getWriteMethod());
                    attributeMap.put(featureName, new ObjectFeatureAgentAttribute(agentObj, readMethod, writeMethod));
                }
            }
        } catch (IntrospectionException e) {
            msgCenter.error("Unable to create feature attributes from agent", e);
        }
    }
