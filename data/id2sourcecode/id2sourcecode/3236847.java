    private void formWindowOpened() {
        try {
            readSignatureDataFromMetaData();
        } catch (java.lang.Exception e) {
            writeToLogs("[info] Error reading MetaData.", true);
        }
        if (signatureReadFromMetaData == null) {
            writeToLogs("[info] This document is not signed.", true);
            saveFromMetaData.setEnabled(false);
        } else {
            writeToLogs("[info] This document appears to have been signed.", false);
            saveFromMetaData.setEnabled(true);
        }
    }
