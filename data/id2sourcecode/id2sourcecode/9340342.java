            private long getLastModified() {
                InputStream in = null;
                try {
                    URLConnection conn = url.openConnection();
                    conn.connect();
                    in = conn.getInputStream();
                    return conn.getLastModified();
                } catch (IOException ioe) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, "Unable to check JAR timestamp.", ioe);
                    }
                    return this.timestamp;
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException ignored) {
                        }
                    }
                }
            }
