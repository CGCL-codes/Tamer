    public java.net.HttpURLConnection connect(java.util.Map<String, String> getParams, java.util.Map<String, ? extends Object> postParams, java.util.Map<String, String> reqProps) throws IOException {
        java.net.HttpURLConnection conn = null;
        try {
            conn = getConnection(getParams);
            if (reqProps != null) for (java.util.Map.Entry<String, String> p : reqProps.entrySet()) conn.setRequestProperty(p.getKey(), p.getValue());
            if (postParams != null) {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.connect();
                java.io.OutputStreamWriter wr;
                java.io.OutputStream outStream = conn.getOutputStream();
                wr = new java.io.OutputStreamWriter(outStream);
                boolean first = true;
                for (java.util.Map.Entry<String, ? extends Object> p : postParams.entrySet()) {
                    if (!first) wr.write('&');
                    first = false;
                    wr.write(p.getKey());
                    wr.write('=');
                    if (p.getValue() instanceof String) wr.write((String) p.getValue()); else if (p.getValue() instanceof java.io.Reader) {
                        java.io.Reader reader = (java.io.Reader) p.getValue();
                        int read = reader.read();
                        while (read >= 0) {
                            wr.write(read);
                            read = reader.read();
                        }
                        reader.close();
                    } else if (p.getValue() instanceof java.io.InputStream) {
                        wr.flush();
                        java.io.InputStream input = (java.io.InputStream) p.getValue();
                        int read = input.read();
                        while (read >= 0) {
                            outStream.write(read);
                            read = input.read();
                        }
                        input.close();
                        outStream.flush();
                    } else throw new IllegalArgumentException("Unrecognized post parameter value type: " + (p.getValue() == null ? "null" : p.getValue().getClass().getName()));
                }
                wr.close();
            } else {
                conn.setRequestMethod("GET");
                conn.connect();
            }
            java.util.Map<String, String> cookies = theCookies;
            if (cookies != null) {
                java.util.List<String> reqCookies = conn.getHeaderFields().get("Set-Cookie");
                if (reqCookies != null) for (String c : reqCookies) {
                    String[] cSplit = c.split(";");
                    for (String cs : cSplit) {
                        cs = cs.trim();
                        int eqIdx = cs.indexOf('=');
                        if (eqIdx >= 0) cookies.put(cs.substring(0, eqIdx), cs.substring(eqIdx + 1)); else cookies.put(cs, "true");
                    }
                }
            }
            return conn;
        } catch (IOException e) {
            if (conn == null || conn.getResponseCode() == 200) throw e;
            HttpResponseException toThrow = new HttpResponseException(e.getMessage(), conn.getResponseCode(), conn.getResponseMessage());
            toThrow.setStackTrace(e.getStackTrace());
            throw toThrow;
        } catch (Throwable e) {
            IOException toThrow = new IOException("Call to " + theURL + " failed: " + e);
            toThrow.setStackTrace(e.getStackTrace());
            throw toThrow;
        }
    }
