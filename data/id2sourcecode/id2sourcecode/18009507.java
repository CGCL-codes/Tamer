    public SourceDataSource(String name, String contentType, StreamSource data) {
        this.name = name;
        this.contentType = contentType == null ? CONTENT_TYPE : contentType;
        os = new ByteArrayOutputStream();
        try {
            if (data != null) {
                Reader reader = data.getReader();
                if (reader != null) {
                    reader = new BufferedReader(reader);
                    int ch;
                    while ((ch = reader.read()) != -1) {
                        os.write(ch);
                    }
                } else {
                    InputStream is = data.getInputStream();
                    if (is == null) {
                        String id = data.getSystemId();
                        if (id != null) {
                            URL url = new URL(id);
                            is = url.openStream();
                        }
                    }
                    if (is != null) {
                        is = new BufferedInputStream(is);
                        byte[] bytes = null;
                        int avail;
                        while ((avail = is.available()) > 0) {
                            if (bytes == null || avail > bytes.length) bytes = new byte[avail];
                            is.read(bytes, 0, avail);
                            os.write(bytes, 0, avail);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }
