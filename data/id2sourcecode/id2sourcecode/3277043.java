    public java.io.OutputStream uploadData(String fileName, String mimeType, java.util.Map<String, String> getParams, java.util.Map<String, ? extends Object> formParams, java.util.Map<String, String> reqProps, final java.io.OutputStream receiver) throws IOException {
        final java.net.HttpURLConnection conn = getConnection(getParams);
        conn.setRequestProperty("Accept-Charset", java.nio.charset.Charset.forName("UTF-8").name());
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setDefaultUseCaches(false);
        if (reqProps != null) for (java.util.Map.Entry<String, String> p : reqProps.entrySet()) conn.setRequestProperty(p.getKey(), p.getValue());
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        final java.io.OutputStream os;
        final java.io.Writer out;
        if (formParams != null && !formParams.isEmpty()) {
            conn.setRequestMethod("POST");
            conn.connect();
            os = conn.getOutputStream();
            out = new prisms.util.LoggingWriter(new java.io.OutputStreamWriter(os, java.nio.charset.Charset.forName("UTF-8")), null);
            out.write("--");
            out.write(BOUNDARY);
            out.write("\r\n");
            for (java.util.Map.Entry<String, ? extends Object> p : formParams.entrySet()) {
                out.write("Content-Disposition: form-data; name=\"" + p.getKey() + "\"\r\n\r\n");
                if (p.getValue() instanceof String) out.write((String) p.getValue()); else if (p.getValue() instanceof java.io.Reader) {
                    java.io.Reader reader = (java.io.Reader) p.getValue();
                    int read = reader.read();
                    while (read >= 0) {
                        out.write(read);
                        read = reader.read();
                    }
                    reader.close();
                } else if (p.getValue() instanceof java.io.InputStream) throw new IllegalArgumentException("InputStreams may not be post parameters for uploading data"); else throw new IllegalArgumentException("Unrecognized post parameter value type: " + (p.getValue() == null ? "null" : p.getValue().getClass().getName()));
                out.write("\r\n--");
                out.write(BOUNDARY);
                out.write("\r\n");
            }
        } else {
            conn.setRequestMethod("GET");
            conn.connect();
            os = conn.getOutputStream();
            out = new prisms.util.LoggingWriter(new java.io.OutputStreamWriter(os, java.nio.charset.Charset.forName("UTF-8")), null);
            out.write("--");
            out.write(BOUNDARY);
            out.write("\r\n");
        }
        out.write("Content-Disposition: form-data; name=\"Upload Data\"; filename=\"" + fileName + "\"");
        out.write("\r\n");
        out.write("Content-Type: ");
        if (mimeType != null) out.write(mimeType); else out.write("application/octet-stream");
        out.write("\r\n");
        out.write("\r\n");
        out.flush();
        return new java.io.OutputStream() {

            private boolean isClosed;

            @Override
            public void write(int b) throws IOException {
                os.write(b);
            }

            @Override
            public void write(byte[] b) throws IOException {
                os.write(b);
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                os.write(b, off, len);
            }

            @Override
            public void flush() throws IOException {
                os.flush();
            }

            @Override
            public void close() throws IOException {
                if (isClosed) return;
                isClosed = true;
                os.flush();
                out.write("\r\n");
                out.write("--");
                out.write(BOUNDARY);
                out.write("--");
                out.write("\r\n");
                out.flush();
                out.close();
                java.io.InputStream in = conn.getInputStream();
                int read = in.read();
                while (read >= 0) {
                    if (receiver != null) receiver.write(read);
                    read = in.read();
                }
                in.close();
            }
        };
    }
