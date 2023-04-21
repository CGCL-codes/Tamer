    private int deriveErrorValue(int minValue, int maxValue, int customErrorValue, Element elementWithErrorValueAttr) {
        int errorValue = 0;
        String errorValueAttr = elementWithErrorValueAttr.getAttribute(NitfProcessorConfig.ERROR_VALUE);
        if (errorValueAttr.equals(NitfProcessorConfig.MIN_VALUE)) {
            errorValue = minValue;
        } else if (errorValueAttr.equals(NitfProcessorConfig.MAX_VALUE)) {
            errorValue = maxValue;
        } else if (errorValueAttr.equals(NitfProcessorConfig.MIN_MAX_VALUE_MIDPOINT)) {
            errorValue = (minValue + maxValue) / 2;
        } else if (errorValueAttr.equals(NitfProcessorConfig.CUSTOM_ERROR_VALUE)) {
            errorValue = customErrorValue;
        } else {
            errorValue = 0;
        }
        return errorValue;
    }
