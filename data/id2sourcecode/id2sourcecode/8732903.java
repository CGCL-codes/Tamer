    public byte[] getResource(String path) {
        Content content = this.get(path);
        if (null != content) {
            return content.bits;
        } else {
            try {
                InputStream resource = this.getClass().getResourceAsStream(path);
                if (null != resource) {
                    try {
                        ByteArrayOutputStream buf = new ByteArrayOutputStream();
                        {
                            byte[] iob = new byte[512];
                            int read;
                            while (0 < (read = resource.read(iob, 0, 512))) {
                                buf.write(iob, 0, read);
                            }
                        }
                        content = new Content(buf.toByteArray());
                        this.put(path, content);
                        return content.bits;
                    } finally {
                        resource.close();
                    }
                }
            } catch (Exception any) {
            }
            return null;
        }
    }
