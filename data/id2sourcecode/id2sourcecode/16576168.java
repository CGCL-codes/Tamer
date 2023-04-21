    @Code(Check.TODO)
    public void close() throws java.io.IOException {
        HttpMessage containerWrite = this.containerWrite;
        if (null != containerWrite) {
            try {
                URL url = this.url;
                if (null != url) {
                    URLConnection connection = url.openConnection();
                    connection.setDoOutput(true);
                    if (connection instanceof alto.net.Connection) {
                        alto.net.Connection nc = (alto.net.Connection) connection;
                        nc.setReference(this);
                        nc.write(containerWrite);
                        return;
                    } else throw new alto.sys.Error.Bug(this.toString());
                }
                File storage = this.getStorage();
                if (null != storage) {
                    if (containerWrite.maySetAuthenticationMethodStore()) {
                        if (containerWrite.maySetContext()) {
                            if (containerWrite.authSign()) {
                                if (storage.write(containerWrite)) return; else throw new alto.sys.Error.State("Write failed");
                            } else throw new alto.sys.Error.State("Failed authentication");
                        } else if (containerWrite.authSign()) {
                            if (storage.write(containerWrite)) return; else throw new alto.sys.Error.State("Write failed");
                        } else throw new alto.sys.Error.State("Failed authentication");
                    } else throw new alto.sys.Error.State("Missing authentication method");
                } else throw new alto.sys.Error.Bug(this.toString());
            } finally {
                this.containerWrite = null;
            }
        }
    }
