    @Override
    protected final void checkIntegrity() throws InternetSCSIException {
        String exceptionMessage;
        do {
            if (!writeExpectedFlag && !protocolDataUnit.getBasicHeaderSegment().isFinalFlag()) {
                exceptionMessage = "W and F flag cannot both be 0.";
                break;
            }
            if (expectedDataTransferLength != 0 && !(readExpectedFlag || writeExpectedFlag)) {
                exceptionMessage = "The ExpectedDataTransferLength is greater than 0, so Read or/and Write Flag has to be set.";
                break;
            }
            return;
        } while (false);
        throw new InternetSCSIException(exceptionMessage);
    }
