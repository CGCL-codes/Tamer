    private void sendResponse(String status, String mime, Properties header, InputStream data) {
        try {
            if (status == null) {
                throw new Error("sendResponse(): Status can't be null.");
            }
            log.info("responde: " + status);
            OutputStream out = mySocket.getOutputStream();
            OutputStreamWriter pw = new OutputStreamWriter(out, codificacion);
            pw.write("HTTP/1.1 " + status + " \r\n");
            if (mime != null) {
                pw.write("Content-Type: " + mime + "\r\n");
            }
            pw.write("Server: NebliServer 0.2\r\n");
            Date ahora = new Date();
            if (header == null || header.getProperty("Date") == null) {
                pw.write("Date: " + HTTPConstantes.gmtFrmt.format(ahora) + "\r\n");
            }
            if (header != null) {
                Enumeration<Object> e = header.keys();
                while (e.hasMoreElements()) {
                    String key = (String) e.nextElement();
                    String value = header.getProperty(key);
                    pw.write(key + ": " + value + "\r\n");
                }
            }
            pw.write("Connection: close\r\n");
            pw.write("\r\n");
            pw.flush();
            if (data != null) {
                byte[] buff = new byte[2048];
                while (true) {
                    int read = data.read(buff, 0, 2048);
                    if (read <= 0) {
                        break;
                    }
                    out.write(buff, 0, read);
                }
            } else {
                log.error("sin datos!!!");
            }
            pw.flush();
            pw.close();
            out.flush();
            out.close();
            if (data != null) {
                data.close();
            }
        } catch (IOException ioe) {
            log.error(ioe.toString());
            System.out.println(ioe.toString());
        } finally {
            try {
                mySocket.close();
            } catch (IOException e) {
            }
        }
    }
