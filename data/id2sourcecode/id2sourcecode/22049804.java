    public MessageDataSource(InputStream in, String contentType) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int readByte = 0;
            while ((readByte = in.read()) != -1) {
                out.write(readByte);
            }
            out.flush();
            this.bytes = out.toByteArray();
            this.contentType = contentType;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
