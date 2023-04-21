    public int checkReadWriteExecute(Object action, Object user) {
        SecurityType type = getAccessRights(action, user);
        boolean result;
        int resultInt = -1;
        if (type == NONE) {
            resultInt = 7;
        } else {
            result = type.hasWriteAccess();
            if (result) resultInt = WRITEBIT;
            if (type.hasReadAccess()) resultInt += READBIT;
            if (type.hasExecuteAccess()) resultInt += EXECUTEBIT;
        }
        if (verbose) System.out.println("Check read write access " + resultInt);
        return resultInt;
    }
