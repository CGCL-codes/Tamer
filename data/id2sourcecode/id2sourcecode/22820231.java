    void writeToStream(final OutputStream output) {
        final String content_disposition = String.format("Content-Disposition: %s\r\n", this.disposition);
        final String content_type = String.format("Content-Type: %s\r\n", this.type);
        try {
            output.write(content_disposition.getBytes("UTF-8"));
            output.write(content_type.getBytes("UTF-8"));
            output.write(MultipartData.CRLF);
            if (input_sequence != null) {
                output.write(input_sequence.getBytes("UTF-8"));
                output.write(MultipartData.CRLF);
            } else if (input_stream != null) {
                final byte[] buff = new byte[1024];
                int read = 0;
                while ((read = input_stream.read(buff)) != -1) {
                    output.write(buff, 0, read);
                }
                output.write(MultipartData.CRLF);
                input_stream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
