    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ActionstepPackage.GET_CALL_INFO__CALL1:
                if (resolve) return getCall1();
                return basicGetCall1();
            case ActionstepPackage.GET_CALL_INFO__ACCOUNT_CODE_VAR:
                return getAccountCodeVar();
            case ActionstepPackage.GET_CALL_INFO__CALLER_ID_NAME_VAR:
                return getCallerIdNameVar();
            case ActionstepPackage.GET_CALL_INFO__CALLER_ID_NUM_VAR:
                return getCallerIdNumVar();
            case ActionstepPackage.GET_CALL_INFO__CHANNEL_NAME_VAR:
                return getChannelNameVar();
            case ActionstepPackage.GET_CALL_INFO__CONTEXT_VAR:
                return getContextVar();
            case ActionstepPackage.GET_CALL_INFO__EXTENSION_VAR:
                return getExtensionVar();
            case ActionstepPackage.GET_CALL_INFO__DIALED_NUMBER:
                return getDialedNumber();
            case ActionstepPackage.GET_CALL_INFO__PRIORITY_VAR:
                return getPriorityVar();
            case ActionstepPackage.GET_CALL_INFO__STATE_VAR:
                return getStateVar();
            case ActionstepPackage.GET_CALL_INFO__UNIQUE_ID_VAR:
                return getUniqueIdVar();
            case ActionstepPackage.GET_CALL_INFO__ANI2_VAR:
                return getAni2Var();
            case ActionstepPackage.GET_CALL_INFO__RDNIS:
                return getRdnis();
            case ActionstepPackage.GET_CALL_INFO__TYPE:
                return getType();
        }
        return super.eGet(featureID, resolve, coreType);
    }
