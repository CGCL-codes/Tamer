        public OutputStream openStream() throws LoggerException {
            OutputStream os;
            String protocol;
            protocol = url_.getProtocol().toLowerCase();
            try {
                if (Configuration.OUTPUT_PROTOCOL_IDS_FILE.indexOf(protocol) >= 0) {
                    os = new FileOutputStream(url_.getPath(), false);
                } else if (Configuration.OUTPUT_PROTOCOL_IDS_SOCKET.indexOf(protocol) >= 0) {
                    os = new Socket(url_.getHost(), url_.getPort()).getOutputStream();
                } else {
                    os = new java.net.URL(protocol, url_.getHost(), url_.getPort(), url_.getPath()).openConnection().getOutputStream();
                }
            } catch (Exception e) {
                throw new LoggerException("Failed opening output stream: " + url_);
            }
            return os;
        }
