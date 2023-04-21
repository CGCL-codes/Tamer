    protected int readApplicationData(byte[] buf, int offset, int len) throws IOException {
        while (applicationDataQueue.size() == 0) {
            if (this.failedWithError) {
                bwmorg.LOG.info("TlsProtocolHandler: readApplicationData() - Unable to read data due to previous error.");
                throw new IOException("TLS readApplicationData");
            }
            if (this.closed) {
                return -1;
            }
            try {
                rs.readData();
            } catch (IOException e) {
                if (!this.closed) {
                    bwmorg.LOG.info("TlsProtocolHandler: readApplicationData() - Error: IOException thrown during writeMessage.");
                    this.failWithError(AL_fatal, AP_internal_error);
                }
                throw e;
            } catch (RuntimeException e) {
                if (!this.closed) {
                    bwmorg.LOG.info("TlsProtocolHandler: readApplicationData() - Error: Runtime Exception thrown during writeMessage.");
                    this.failWithError(AL_fatal, AP_internal_error);
                }
                throw e;
            } catch (UnknownDataException e) {
                return -1;
            }
        }
        len = Math.min(len, applicationDataQueue.size());
        applicationDataQueue.read(buf, offset, len, 0);
        applicationDataQueue.removeData(len);
        return len;
    }
