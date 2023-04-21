    public ConditionResult evaluate() throws ResponsiveException {
        try {
            State currentState = getSensor().readFromSensor(getChannel()).getDataObject();
            if (currentState.equals(testState)) {
                return new StateConditionResult(Boolean.TRUE, currentState);
            } else {
                return new StateConditionResult(Boolean.FALSE, currentState);
            }
        } catch (HardwareException he) {
            logger.error("Hardware exception while evaluating state condition: " + he.getMessage());
            throw new ResponsiveException(he);
        }
    }
