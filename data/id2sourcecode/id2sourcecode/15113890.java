    public HashMap<String, String> getParametersForUIComponent(Component comp) {
        HashMap<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put(PARAMETER_MEASUREMENT_CHANNEL, ((DDSSweepPanel) comp).getMeasurement().getCommChannel().getChannelName() + "");
        parameterMap.put(PARAMETER_MEASUREMENT_ADDRESS, ((DDSSweepPanel) comp).getMeasurement().getAddress() + "");
        return parameterMap;
    }
