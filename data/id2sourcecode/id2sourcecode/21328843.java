    public String menu(String announce, String emptyAnnounce, String errorAnnounce, boolean replayAnnounce, int inputCount, String validInputs[]) throws Exception {
        String digits = "";
        String errorType = "";
        setTimeoutCount(0);
        setErrorCount(0);
        int max = 0;
        for (int i = 0; i < validInputs.length; i++) {
            if (validInputs[i].length() > max) {
                max = validInputs[i].length();
            }
        }
        while ((getErrorCount() < inputCount) && (NullStatus.isNull(digits))) {
            if ((getErrorCount() == 0) || replayAnnounce) {
                digits = TegsoftPBX.readInput(getChannel(), announce, true, 4, 2, max);
            } else {
                digits = TegsoftPBX.readInput(getChannel(), null, true, 4, 2, max);
            }
            if (NullStatus.isNull(digits)) {
                incTimeoutCount();
                incErrorCount();
                errorType = "TO";
                if ((getErrorCount() < inputCount)) {
                    TegsoftPBX.playBackground(getChannel(), emptyAnnounce);
                }
            } else if (digits.length() > max) {
                digits = "";
                incErrorCount();
                errorType = "INV";
                if ((getErrorCount() < inputCount)) {
                    TegsoftPBX.playBackground(getChannel(), errorAnnounce);
                }
            } else {
                for (int i = 0; i < validInputs.length; i++) {
                    if (Compare.equal(validInputs[i], digits)) {
                        return validInputs[i];
                    }
                }
                digits = "";
                incErrorCount();
                errorType = "INV";
                if ((getErrorCount() < inputCount)) {
                    TegsoftPBX.playBackground(getChannel(), errorAnnounce);
                }
            }
        }
        return errorType;
    }
