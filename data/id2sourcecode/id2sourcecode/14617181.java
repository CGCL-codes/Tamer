    public ConditionResult evaluate() throws ResponsiveException {
        ValueData data;
        try {
            data = getSensor().readFromSensor(getChannel()).getDataObject();
        } catch (HardwareException he) {
            logger.error("Error while reading value from sensor: " + he.getMessage());
            throw new ResponsiveException("Error while evaluating condition: cannot read from sensor: ", he);
        }
        try {
            switch(testOperator) {
                case GREATER_THAN:
                    if (data.compareTo(testValue) > 0) return new ValueConditionResult(Boolean.TRUE, data);
                    break;
                case LESS_THAN:
                    if (data.compareTo(testValue) < 0) return new ValueConditionResult(Boolean.TRUE, data);
                    break;
                default:
                    logger.debug("Unknown operator type: " + testOperator);
            }
        } catch (ClassCastException cce) {
            logger.error("Error while evaluating condition: incompatible units");
            throw new ResponsiveException(cce);
        }
        return new ValueConditionResult(Boolean.FALSE, data);
    }
