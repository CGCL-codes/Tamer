    private Object[] convertBindTupleVals(String[] strBindValues, BLOB pTempBlob, UCon pUCon, UploadInfo pUploadInfo) {
        try {
            Object newBindTupleValues[] = new Object[strBindValues.length / 2];
            for (int n = 0; n < strBindValues.length / 2; n++) {
                if (strBindValues[n * 2].equals(StoreLocation.CLOB_USING_TYPE)) {
                    CLOB temporaryCLOB = null;
                    if (pTempBlob == null) temporaryCLOB = CLOB.empty_lob(); else {
                        temporaryCLOB = pUCon.getTemporaryClob();
                        InputStream lClobIS = temporaryCLOB.getStream();
                        OutputStream lBlobOS = pTempBlob.getBinaryOutputStream();
                        IOUtil.transfer(lClobIS, lBlobOS, 32768);
                        IOUtil.close(lClobIS);
                        IOUtil.close(lBlobOS);
                    }
                    newBindTupleValues[n] = temporaryCLOB;
                } else if (strBindValues[n * 2].equals(StoreLocation.BLOB_USING_TYPE)) {
                    BLOB temporaryBLOB = null;
                    if (pTempBlob == null) temporaryBLOB = BLOB.empty_lob(); else {
                        temporaryBLOB = pTempBlob;
                    }
                    newBindTupleValues[n] = temporaryBLOB;
                } else if (strBindValues[n * 2].equals(StoreLocation.XMLTYPE_USING_TYPE)) {
                    CLOB temporaryCLOB = null;
                    if (pTempBlob == null) temporaryCLOB = CLOB.empty_lob(); else {
                        temporaryCLOB = pUCon.getTemporaryClob();
                        InputStream lClobIS = temporaryCLOB.getStream();
                        OutputStream lBlobOS = pTempBlob.getBinaryOutputStream();
                        IOUtil.transfer(lClobIS, lBlobOS, 32768);
                        IOUtil.close(lClobIS);
                        IOUtil.close(lBlobOS);
                    }
                    XMLType temporaryXMLType = pUCon.createXmlType(temporaryCLOB);
                    newBindTupleValues[n] = temporaryXMLType;
                } else if (strBindValues[n * 2].equals(StoreLocation.FILE_METADATA_XMLTYPE)) {
                    DOM metaDataDOM = DOM.createDocument("file-metadata");
                    XThread.writeUploadMetadataToDOM(metaDataDOM, pUploadInfo, false, false);
                    String metaDataDocStr = metaDataDOM.outputDocumentToString();
                    CLOB clob = pUCon.getTemporaryClob();
                    clob.trim(0);
                    Writer writer = clob.getCharacterOutputStream();
                    Reader reader = new StringReader(metaDataDocStr);
                    IOUtil.transfer(reader, writer, 32768);
                    IOUtil.close(writer);
                    XMLType temporaryFileInfoXMLType = pUCon.createXmlType(clob);
                    newBindTupleValues[n] = temporaryFileInfoXMLType;
                } else {
                    newBindTupleValues[n] = strBindValues[n * 2 + 1];
                }
            }
            return newBindTupleValues;
        } catch (Throwable th) {
            throw new ExInternal("Unexpected error uploading file content - see nested exception for further information.", th);
        }
    }
