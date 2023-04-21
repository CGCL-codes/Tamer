    public void setAttributes(Map attrs) throws DctmFileException {
        if (this.attributes == null) {
            this.loadAttributes();
        }
        Iterator keyIterator = attrs.keySet().iterator();
        while (keyIterator.hasNext()) {
            String name = keyIterator.next().toString();
            Object value = attrs.get(name);
            if (this.attributes.containsKey(name)) {
                this.attributes.put(name, value);
            }
        }
        File tmpFile = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = this.metaClient.getAttributeSerializer().serialize(this);
            tmpFile = File.createTempFile("dctmvfs", ".tmp");
            outputStream = new FileOutputStream(tmpFile);
            int bufferSize = 1024 * 4;
            byte[] buffer = new byte[bufferSize];
            int read = -1;
            while ((read = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, read);
            }
            inputStream.close();
            inputStream = null;
            outputStream.close();
            outputStream = null;
            this.wrappedMetaFile.setFileContent(tmpFile);
        } catch (Exception e) {
            throw new DctmFileException("Error serializing attributes", e);
        } finally {
            if (tmpFile != null && tmpFile.exists()) {
                tmpFile.delete();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.warn("Error closing inputstream", e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.warn("Error closing outputstream", e);
                }
            }
        }
    }
