    public String readInput(String announce, String emptyAnnounce, String errorAnnounce, boolean replayAnnounce, boolean interruptible, int inputCount, int initial_timeout, int interdigit_timeout, int mindigits, int maxdigits) throws Exception {
        String digits = "";
        String errorType = "";
        setTimeoutCount(0);
        setErrorCount(0);
        if (inputCount == 0) {
            throw new Exception("ERROR!!");
        }
        while ((getErrorCount() < inputCount) && (NullStatus.isNull(digits))) {
            if ((getErrorCount() == 0) || replayAnnounce) {
                digits = TegsoftPBX.readInput(getChannel(), announce, interruptible, initial_timeout, initial_timeout, maxdigits);
            } else {
                digits = TegsoftPBX.readInput(getChannel(), announce, interruptible, initial_timeout, initial_timeout, maxdigits);
            }
            if (NullStatus.isNull(digits)) {
                incTimeoutCount();
                incErrorCount();
                errorType = "TO";
                TegsoftPBX.logMessage(getChannel(), Level.DEBUG, "GOT timeout");
                if ((getErrorCount() < inputCount)) {
                    TegsoftPBX.playBackground(getChannel(), emptyAnnounce);
                }
            } else if (digits.length() > maxdigits) {
                TegsoftPBX.logMessage(getChannel(), Level.DEBUG, "GOT invalid entry more than MAX " + digits);
                digits = "";
                incErrorCount();
                errorType = "INV";
                if ((getErrorCount() < inputCount)) {
                    TegsoftPBX.playBackground(getChannel(), errorAnnounce);
                }
            } else if (digits.length() < mindigits) {
                TegsoftPBX.logMessage(getChannel(), Level.DEBUG, "GOT invalid entry less than MIN " + digits);
                digits = "";
                incErrorCount();
                errorType = "INV";
                if ((getErrorCount() < inputCount)) {
                    TegsoftPBX.playBackground(getChannel(), errorAnnounce);
                }
            } else {
                TegsoftPBX.logMessage(getChannel(), Level.DEBUG, "Returning " + digits);
                return digits;
            }
        }
        return errorType;
    }
