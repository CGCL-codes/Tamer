    protected InputStream getContentStream() throws MessagingException {
        try {
            InputStream handlerStream = getInputStream();
            InternetHeaders tmpHeaders = new InternetHeaders(handlerStream);
            byte[] buf;
            int len;
            int size = 1024;
            if (handlerStream instanceof ByteArrayInputStream) {
                size = handlerStream.available();
                buf = new byte[size];
                len = handlerStream.read(buf, 0, size);
            } else {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                buf = new byte[size];
                while ((len = handlerStream.read(buf, 0, size)) != -1) bos.write(buf, 0, len);
                buf = bos.toByteArray();
            }
            return new ByteArrayInputStream(buf);
        } catch (java.io.IOException ioe) {
            throw new MessagingException("Error getting Content Stream", ioe);
        }
    }
