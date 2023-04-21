    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ActionstepPackage.ORIGINATE_CALL__NEW_CALL1:
                return getNewCall1();
            case ActionstepPackage.ORIGINATE_CALL__ASYNC:
                return isAsync();
            case ActionstepPackage.ORIGINATE_CALL__ACCOUNT:
                return getAccount();
            case ActionstepPackage.ORIGINATE_CALL__APPLICATION:
                return getApplication();
            case ActionstepPackage.ORIGINATE_CALL__CALLER_ID:
                return getCallerId();
            case ActionstepPackage.ORIGINATE_CALL__CONTEXT:
                return getContext();
            case ActionstepPackage.ORIGINATE_CALL__DATA:
                return getData();
            case ActionstepPackage.ORIGINATE_CALL__EXTENSION:
                return getExtension();
            case ActionstepPackage.ORIGINATE_CALL__PRIORITY:
                return getPriority();
            case ActionstepPackage.ORIGINATE_CALL__TIMEOUT:
                return getTimeout();
            case ActionstepPackage.ORIGINATE_CALL__CALLING_PRESENTATION:
                return getCallingPresentation();
            case ActionstepPackage.ORIGINATE_CALL__CHANNEL:
                return getChannel();
            case ActionstepPackage.ORIGINATE_CALL__TAKE_CONTROL:
                return isTakeControl();
            case ActionstepPackage.ORIGINATE_CALL__VARIABLES:
                return getVariables();
        }
        return super.eGet(featureID, resolve, coreType);
    }
