    private String fromStateCondition(StateCondition stateCondition) throws ResponsiveConverterException {
        StringBuffer conditionStr = new StringBuffer();
        conditionStr.append(Identifiers.CONDITION_START.getIdentifier());
        conditionStr.append(Identifiers.CONDITION.getIdentifier());
        conditionStr.append(Identifiers.NAME.getIdentifier());
        conditionStr.append(Delimeters.KEYVALUE.getDelimeter());
        conditionStr.append(stateCondition.getConditionName());
        conditionStr.append(Delimeters.KEYVALUE_PAIR.getDelimeter());
        conditionStr.append(Identifiers.HW_ADDR_REF.getIdentifier());
        conditionStr.append(Delimeters.KEYVALUE.getDelimeter());
        conditionStr.append(stateCondition.getHardwareAddr());
        conditionStr.append(Delimeters.KEYVALUE_PAIR.getDelimeter());
        conditionStr.append(Identifiers.IO_CHANNEL.getIdentifier());
        conditionStr.append(Delimeters.KEYVALUE.getDelimeter());
        conditionStr.append(stateCondition.getChannel().toString());
        conditionStr.append(Delimeters.KEYVALUE_PAIR.getDelimeter());
        conditionStr.append(Identifiers.TYPE.getIdentifier());
        conditionStr.append(Delimeters.KEYVALUE.getDelimeter());
        conditionStr.append(Inputs.STATE_CONDITION.asString());
        conditionStr.append(Delimeters.KEYVALUE_PAIR.getDelimeter());
        conditionStr.append(Identifiers.TEST_STATE.getIdentifier());
        conditionStr.append(Delimeters.KEYVALUE.getDelimeter());
        conditionStr.append(stateCondition.getTestState().toString());
        conditionStr.append(Identifiers.CONDITION_STOP.getIdentifier());
        return conditionStr.toString();
    }
