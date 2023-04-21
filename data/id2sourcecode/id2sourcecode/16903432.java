    private Object[] convertBindTupleVals(String[] strBindValues, UCon connection, Object pLOB, FileUploadInfo fileUploadInfo) {
        try {
            Object newBindTupleValues[] = new Object[strBindValues.length / 2];
            for (int n = 0; n < strBindValues.length / 2; n++) {
                if (strBindValues[n * 2].equals(StoreLocation.CLOB_USING_TYPE)) {
                    CLOB temporaryCLOB = (CLOB) pLOB;
                    newBindTupleValues[n] = temporaryCLOB;
                } else if (strBindValues[n * 2].equals(StoreLocation.BLOB_USING_TYPE)) {
                    BLOB temporaryBLOB = (BLOB) pLOB;
                    newBindTupleValues[n] = temporaryBLOB;
                } else if (strBindValues[n * 2].equals(StoreLocation.XMLTYPE_USING_TYPE)) {
                    CLOB temporaryCLOB = (CLOB) pLOB;
                    XMLType temporaryXMLType = connection.createXmlType(temporaryCLOB);
                    newBindTupleValues[n] = temporaryXMLType;
                } else if (strBindValues[n * 2].equals(StoreLocation.FILE_METADATA_XMLTYPE)) {
                    DOM metaDataDOM = DOM.createDocument("file-metadata");
                    new DOM(fileUploadInfo.serialiseFileMetadataTOXML().getDocumentElement()).copyContentsTo(metaDataDOM);
                    String metaDataDocStr = metaDataDOM.outputDocumentToString();
                    CLOB clob = connection.getTemporaryClob();
                    clob.trim(0);
                    Writer writer = clob.getCharacterOutputStream();
                    Reader reader = new StringReader(metaDataDocStr);
                    IOUtil.transfer(reader, writer, 32768);
                    IOUtil.close(writer);
                    XMLType temporaryFileInfoXMLType = connection.createXmlType(clob);
                    newBindTupleValues[n] = temporaryFileInfoXMLType;
                } else {
                    newBindTupleValues[n] = strBindValues[n * 2 + 1];
                }
            }
            return newBindTupleValues;
        } catch (Throwable th) {
            throw new ExInternal("Unexpected error uploading file content - see nested exception for " + "further information.", th);
        }
    }
