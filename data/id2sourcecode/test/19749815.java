        private void sendResponse(String status, String mime, Properties header, String payload) {
            InputStream data = new ByteArrayInputStream(payload.getBytes());
            try {
                if (status == null) throw new Error("sendResponse(): Status can't be null.");
                OutputStream out = mySocket.getOutputStream();
                PrintWriter pw = new PrintWriter(out);
                pw.print("HTTP/1.0 " + status + " \r\n");
                if (mime != null) pw.print("Content-Type: " + mime + "\r\n");
                if (header == null || header.getProperty("Date") == null) pw.print("Date: " + gmtFrmt.format(new Date()) + "\r\n");
                if (header != null) {
                    Enumeration e = header.keys();
                    while (e.hasMoreElements()) {
                        String key = (String) e.nextElement();
                        String value = header.getProperty(key);
                        pw.print(key + ": " + value + "\r\n");
                    }
                }
                pw.print("\r\n");
                pw.flush();
                if (data != null) {
                    byte[] buff = new byte[2048];
                    int read = 2048;
                    while (read == 2048) {
                        read = data.read(buff, 0, 2048);
                        out.write((byte[]) buff, 0, read);
                    }
                }
                out.flush();
                out.close();
                if (data != null) data.close();
            } catch (IOException ioe) {
                logger.debug("Could not write on socket");
                try {
                    mySocket.close();
                } catch (Throwable t) {
                }
            }
        }
