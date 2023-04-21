    private void sendBody(OutputStream os) throws java.io.IOException {
        if ((this.bodyInputStream != null) && (this.bodyLength >= 0)) {
            if (!(os instanceof BufferedOutputStream)) {
                os = new BufferedOutputStream(os);
            }
            byte[] buffer = new byte[maxBodyDataBufferSize];
            int read = -1;
            long bytesLeftToRead = this.bodyLength;
            while ((bytesLeftToRead > 0) && ((read = this.bodyInputStream.read(buffer, 0, buffer.length)) >= 0)) {
                os.write(buffer, 0, read);
                bytesLeftToRead -= read;
            }
            try {
                this.bodyInputStream.close();
            } catch (Exception e) {
            }
        } else {
            os.write(body);
        }
        os.flush();
    }
