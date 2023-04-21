        private void sendResponse(final String status, final Properties header, final InputStream data) {
            try {
                if (status == null) {
                    throw new Error("sendResponse(): Status can't be null.");
                }
                OutputStream out = mySocket.getOutputStream();
                PrintWriter pw = new PrintWriter(out);
                pw.print("HTTP/1.0 " + status + " \r\n");
                if (header == null || header.getProperty("date") == null) {
                    pw.print("Date: " + gmtFrmt.format(new Date()) + "\r\n");
                }
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
                    byte[] buff = new byte[READ_BUFFER_SIZE];
                    while (true) {
                        int read = data.read(buff, 0, READ_BUFFER_SIZE);
                        if (read <= 0) {
                            break;
                        }
                        out.write(buff, 0, read);
                    }
                }
                out.flush();
                out.close();
                if (data != null) {
                    data.close();
                }
            } catch (IOException ioe) {
                try {
                    mySocket.close();
                } catch (Throwable t) {
                    log.error("Could not send message to client", t);
                }
            }
        }
