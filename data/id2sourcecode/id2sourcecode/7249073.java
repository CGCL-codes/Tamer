    public void writeTo(OutputStream os) throws IOException {
        if (dataSource != null) {
            InputStream is = null;
            byte data[] = new byte[8 * 1024];
            int bytes_read;
            is = dataSource.getInputStream();
            try {
                while ((bytes_read = is.read(data)) > 0) {
                    os.write(data, 0, bytes_read);
                }
            } finally {
                is.close();
                is = null;
            }
        } else {
            DataContentHandler dch = getDataContentHandler();
            dch.writeTo(object, objectMimeType, os);
        }
    }
