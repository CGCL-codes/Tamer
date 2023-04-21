    public byte[] getData(String key) {
        byte[] result = null;
        String value = getString(key);
        try {
            if (value != null) {
                InputStream stream = this.getClass().getResourceAsStream(value);
                if (stream != null) {
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    try {
                        byte[] chunk = new byte[100];
                        int read = -1;
                        do {
                            read = stream.read(chunk);
                            if (read > 0) {
                                buffer.write(chunk, 0, read);
                            }
                        } while (read != -1);
                    } finally {
                        stream.close();
                    }
                    result = buffer.toByteArray();
                }
            }
        } catch (Exception e) {
        }
        return result;
    }
