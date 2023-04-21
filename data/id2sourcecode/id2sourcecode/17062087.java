    public static ComponentConfig getComponentConfig(String globalName, String rootLocation) throws ComponentException {
        ComponentConfig componentConfig = null;
        if (UtilValidate.isNotEmpty(globalName)) {
            componentConfig = (ComponentConfig) componentConfigs.get(globalName);
        }
        if (componentConfig == null) {
            if (rootLocation != null) {
                synchronized (ComponentConfig.class) {
                    if (UtilValidate.isNotEmpty(globalName)) {
                        componentConfig = (ComponentConfig) componentConfigs.get(globalName);
                    }
                    if (componentConfig == null) {
                        componentConfig = new ComponentConfig(globalName, rootLocation);
                        if (componentConfigs.containsKey(componentConfig.getGlobalName())) {
                            Debug.logWarning("WARNING: Loading ofbiz-component using a global name that already exists, will over-write: " + componentConfig.getGlobalName(), module);
                        }
                        if (componentConfig.enabled()) {
                            componentConfigs.put(componentConfig.getGlobalName(), componentConfig);
                        }
                    }
                }
            } else {
                throw new ComponentException("No component found named : " + globalName);
            }
        }
        return componentConfig;
    }
