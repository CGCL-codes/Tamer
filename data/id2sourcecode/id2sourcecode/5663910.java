    public void request(String method, String content_type, IppRequest request) throws Exception {
        connect();
        server.setDoInput(true);
        server.setDoOutput(true);
        server.setRequestMethod(method);
        server.setRequestProperty("Content-type", content_type);
        server.setAllowUserInteraction(false);
        server.connect();
        doVerbose(2, "IppClient.java: request(): Write data to output stream");
        DataOutputStream bw = new DataOutputStream(new BufferedOutputStream(server.getOutputStream()));
        byte[] data = request.getAgroups().getBytes();
        Object document = request.getDocument();
        bw.write(request.getVersion());
        bw.writeShort(request.getOperationId());
        bw.writeInt(request.getRequestId());
        bw.write(data, 0, data.length);
        doVerbose(2, "IppClient.java: request(): Write header OK");
        if (document != null) {
            if (document instanceof InputStream) {
                InputStream stream = (InputStream) document;
                byte[] buf = new byte[1024 * 8];
                int count = 0;
                doVerbose(2, "IppClient.java: request(): Write document data to output stream from InpuStream");
                while ((count = stream.read(buf, 0, buf.length - 10)) != -1) {
                    doVerbose(2, "IppClient.java: request(): Read " + count + " bytes");
                    bw.write(buf, 0, count);
                    doVerbose(2, "IppClient.java: request(): Wrote " + count + " bytes");
                }
                ((InputStream) document).close();
                doVerbose(2, "IppClient.java: request(): Close InputStream");
            } else if (document instanceof URL) {
                URLConnection urlconnection = ((URL) document).openConnection();
                doVerbose(2, "IppClient.java: request(): Write document data to printer's stream from URL");
                doVerbose(1, "IppClient.java: request(): document to print: " + ((URL) document).toString());
                try {
                    BufferedInputStream stream = new BufferedInputStream(urlconnection.getInputStream());
                    byte[] buf = new byte[1024 * 8];
                    int count = 0;
                    while ((count = stream.read(buf, 0, buf.length)) != -1) {
                        doVerbose(2, "IppClient.java: request(): Read " + count + " bytes from " + stream.toString());
                        bw.write(buf, 0, count);
                        doVerbose(2, "IppClient.java: request(): Wrote " + count + " bytes");
                    }
                    stream.close();
                    doVerbose(2, "IppClient.java: request(): Close InputStream " + stream.toString());
                } catch (IOException e) {
                    if (urlconnection instanceof HttpURLConnection && ((HttpURLConnection) urlconnection).getResponseCode() == 401) {
                        throw new IppException("HTTP/1.x 401 Unauthorized access to \n\t" + ((URL) document).toString());
                    }
                    throw e;
                }
            } else if (document instanceof byte[]) {
                InputStream stream = new ByteArrayInputStream((byte[]) document);
                byte[] buf = new byte[1024 * 8];
                int count = 0;
                while ((count = stream.read(buf, 0, buf.length)) != -1) {
                    bw.write(buf, 0, count);
                }
                stream.close();
            } else if (document instanceof char[]) {
                CharArrayReader stream = new CharArrayReader((char[]) document);
                char[] buf = new char[1024 * 8];
                int count = 0;
                while ((count = stream.read(buf, 0, buf.length)) != -1) {
                    bw.writeChars(new String(buf, 0, count));
                }
                stream.close();
            } else if (document instanceof String) {
                bw.writeChars((String) document);
            } else if (document instanceof Reader) {
                char[] buf = new char[1024 * 8];
                int count = 0;
                while ((count = ((Reader) document).read(buf, 0, buf.length)) != -1) {
                    bw.writeChars(new String(buf, 0, count));
                }
                ((Reader) document).close();
            }
        }
        bw.flush();
        bw.close();
        doVerbose(2, "IppClient.java: request(): Write OK");
    }
