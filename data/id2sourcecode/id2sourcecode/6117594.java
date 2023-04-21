        public void run() {
            try {
                HttpURLConnection con = null;
                for (int i = 0; i < CONNECTION_COUNT; i++) {
                    URL url = new URL(spec);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestProperty("Connection", "close");
                    InputStream is = con.getInputStream();
                    StreamCopier.unsyncCopy(is, null, StreamCopier.DEFAULT_BUFFER_SIZE);
                    is.close();
                    if (con != null) con.disconnect();
                }
                log.info("Finished.");
            } catch (java.io.IOException e) {
                log.error(e, e);
            }
        }
