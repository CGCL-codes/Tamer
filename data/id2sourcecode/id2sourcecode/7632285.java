        private void sendResponse(String status, String mime, Properties header, InputStream data) {
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
                    long start = System.currentTimeMillis();
                    long total = 0;
                    Object mutex = new Object();
                    synchronized (mutex) {
                        byte[] buff = new byte[2048];
                        while (true) {
                            int read = data.read(buff, 0, 2048);
                            if (read <= 0) break;
                            total += read;
                            if ((total / 2048) % 40 == 0) DefaultLog.debug(NanoHTTPD.class, "" + total);
                            out.write(buff, 0, read);
                            long elapsed = System.currentTimeMillis() - start;
                            if (elapsed < total / MAX_BYTES_PER_SECOND) {
                                try {
                                    mutex.wait((total / MAX_BYTES_PER_SECOND) - elapsed);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if ((total / 2048) % 20 != 0) DefaultLog.debug(NanoHTTPD.class, "" + total);
                    }
                }
                out.flush();
                out.close();
                if (data != null) data.close();
            } catch (IOException ioe) {
                try {
                    mySocket.close();
                } catch (Throwable t) {
                }
            }
        }
