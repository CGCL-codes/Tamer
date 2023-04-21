    public void insertDocument(InputStream inputStream, IFilter filter) throws NOAException {
        if (inputStream == null || filter == null) return;
        FileOutputStream outputStream = null;
        File tempFile = null;
        try {
            XDocumentInsertable xDocumentInsertable = (XDocumentInsertable) UnoRuntime.queryInterface(XDocumentInsertable.class, xTextCursor);
            if (xDocumentInsertable != null) {
                boolean useOld = true;
                if (useOld) {
                    byte buffer[] = new byte[0xffff];
                    int bytes = -1;
                    tempFile = File.createTempFile("noatemp" + System.currentTimeMillis(), "tmp");
                    tempFile.deleteOnExit();
                    outputStream = new FileOutputStream(tempFile);
                    while ((bytes = inputStream.read(buffer)) != -1) outputStream.write(buffer, 0, bytes);
                    insertDocument(tempFile.getAbsolutePath());
                } else {
                    PropertyValue[] loadProps = new PropertyValue[2];
                    loadProps[0] = new PropertyValue();
                    loadProps[0].Name = "InputStream";
                    loadProps[0].Value = new ByteArrayXInputStreamAdapter(inputStream, null);
                    loadProps[1] = new PropertyValue();
                    loadProps[1].Name = "FilterName";
                    loadProps[1].Value = filter.getFilterDefinition(textDocument);
                    xDocumentInsertable.insertDocumentFromURL("private:stream", loadProps);
                }
            }
        } catch (Throwable throwable) {
            throw new NOAException(throwable);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ioException) {
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ioException) {
                }
            }
            if (tempFile != null) tempFile.delete();
        }
    }
