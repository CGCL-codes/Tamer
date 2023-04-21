    private void updateUsingSelectedLOB(Object pLOB, ContextUCon contextUCon, FileUploadInfo fileUploadInfo, Object[] resultObjs) throws ExInternal {
        InputStream lobIS = null;
        try {
            if (pLOB instanceof CLOB) {
                lobIS = ((CLOB) pLOB).getAsciiStream();
            } else if (pLOB instanceof BLOB) {
                lobIS = ((BLOB) pLOB).getBinaryStream();
            }
            Object lob = null;
            for (int n = 0; n < resultObjs.length && lob == null; n++) {
                if (resultObjs[n] instanceof CLOB || resultObjs[n] instanceof BLOB) lob = resultObjs[n];
            }
            if (lob == null) {
                throw new ExInternal("The file storage location query, \"" + mStorageLocation.getQueryStatement() + "\" must return a CLOB or BLOB column type.");
            }
            OutputStream os;
            if (lob instanceof CLOB) {
                CLOB clob = (CLOB) lob;
                clob.trim(0);
                os = clob.getAsciiOutputStream();
            } else if (lob instanceof BLOB) {
                BLOB blob = (BLOB) lob;
                blob.trim(0);
                os = blob.getBinaryOutputStream();
            } else {
                throw new ExInternal("Error in file storage location, \"" + mStorageLocation.getName() + "\" - the query returned a type that was not a CLOB or BLOB!");
            }
            byte transferBuf[] = new byte[32000];
            int readCount;
            fileUploadInfo.currentUploadSize = 0;
            while ((readCount = lobIS.read(transferBuf)) != -1) {
                fileUploadInfo.currentUploadSize += readCount;
                os.write(transferBuf, 0, readCount);
                os.flush();
            }
            os.close();
        } catch (IOException ex) {
            throw new ExInternal("Unexpected error occurred during update of file storage location - see nested exception for " + "further information.", mStorageLocation.getXML(), ex);
        } catch (SQLException ex) {
            throw new ExInternal("Unexpected SQL exception during file storage location CLOB/BLOB update - see nested exception for " + "further information.", ex);
        } finally {
            IOUtil.close(lobIS);
        }
    }
