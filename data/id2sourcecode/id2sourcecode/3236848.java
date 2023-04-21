    private void readSignatureDataFromMetaData() throws Exception, BootstrapException, NoSuchAlgorithmException, IOException {
        XDocumentInfo document = null;
        try {
            document = MetaDataReader.getDocumentInfo(xLocalContext);
        } catch (java.lang.Exception ex) {
            writeToLogs("[exception] Exception occured while trying to read MetaData.", true);
        }
        String signatureXString = MetaDataReader.readValueFromMetaData(document, SignItConstants.META_DATA_ARG0);
        String signatureYString = MetaDataReader.readValueFromMetaData(document, SignItConstants.META_DATA_ARG1);
        String signatureDigest = MetaDataReader.readValueFromMetaData(document, SignItConstants.META_DATA_ARG2);
        signatureReadFromMetaData = new SignatureDataIR(Formatter.stringToList(signatureXString, SignItConstants.DELIMITER), Formatter.stringToList(signatureYString, SignItConstants.DELIMITER), 0);
        signatureReadFromMetaData.setDigest(Formatter.stringToByteArray(signatureDigest));
    }
