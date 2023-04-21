    public Response post(String path, List parameters, boolean multipart) throws IOException, SAXException {
        AuthUtilities.addAuthToken(parameters);
        RequestContext requestContext = RequestContext.getRequestContext();
        URL url = UrlUtilities.buildPostUrl(getHost(), getPort(), path);
        HttpURLConnection conn = null;
        try {
            String boundary = "---------------------------7d273f7a0d3";
            conn = (HttpURLConnection) url.openConnection();
            if (proxyAuth) {
                conn.setRequestProperty("Proxy-Authorization", "Basic " + getProxyCredentials());
            }
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            if (multipart) {
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            }
            conn.connect();
            DataOutputStream out = null;
            try {
                if (Zooomr.debugRequest) {
                    out = new DataOutputStream(new DebugOutputStream(conn.getOutputStream(), System.out));
                } else {
                    out = new DataOutputStream(conn.getOutputStream());
                }
                if (multipart) {
                    out.writeBytes("--" + boundary + "\r\n");
                    Iterator iter = parameters.iterator();
                    while (iter.hasNext()) {
                        Parameter p = (Parameter) iter.next();
                        writeParam(p.getName(), p.getValue(), out, boundary);
                    }
                } else {
                    Iterator iter = parameters.iterator();
                    while (iter.hasNext()) {
                        Parameter p = (Parameter) iter.next();
                        out.writeBytes(p.getName());
                        out.writeBytes("=");
                        try {
                            out.writeBytes(URLEncoder.encode(String.valueOf(p.getValue()), UTF8));
                        } catch (UnsupportedEncodingException e) {
                        }
                        if (iter.hasNext()) {
                            out.writeBytes("&");
                        }
                    }
                    Auth auth = requestContext.getAuth();
                    if (auth != null) {
                    }
                }
                out.flush();
            } finally {
                IOUtilities.close(out);
            }
            InputStream in = null;
            try {
                if (Zooomr.debugStream) {
                    in = new DebugInputStream(conn.getInputStream(), System.out);
                } else {
                    in = conn.getInputStream();
                }
                Response response = null;
                synchronized (mutex) {
                    Document document = builder.parse(in);
                    response = (Response) responseClass.newInstance();
                    response.parse(document);
                }
                return response;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtilities.close(in);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
