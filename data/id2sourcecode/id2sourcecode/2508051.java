        public void run() {
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) _url.openConnection();
                if (_headers != null) {
                    for (String header : _headers) {
                        if (header != null && header.indexOf("=") != -1) {
                            int idx = header.indexOf("=");
                            String name = header.substring(0, idx);
                            String value = header.substring(idx + 1);
                            conn.addRequestProperty(name, value);
                        }
                    }
                }
                conn.addRequestProperty("Content-Length", "" + _request.getBytes().length);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                _start = new Date();
                if (_request != null) {
                    conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    os.write(_request.getBytes());
                    os.flush();
                } else {
                    conn.connect();
                }
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), 1024 * 30);
                StringBuffer sb = new StringBuffer();
                for (byte[] b = new byte[1]; bis.read(b) > 0; sb.append((char) b[0])) ;
                bis.close();
                _response = sb.toString();
                if (conn.getHeaderFields() != null) {
                    for (Entry<String, List<String>> entry : conn.getHeaderFields().entrySet()) {
                        StringBuffer e = new StringBuffer();
                        e.append(entry.getKey());
                        e.append("=");
                        for (String v : entry.getValue()) {
                            e.append(v);
                            e.append(";");
                        }
                        if (e.toString().trim().length() > 0) {
                            if (_response_headers == null) {
                                _response_headers = new LinkedList<String>();
                            }
                            _response_headers.add(e.toString());
                        }
                    }
                }
                conn.disconnect();
            } catch (Throwable th) {
                th.printStackTrace();
                _error = th;
                if (conn != null) {
                    try {
                        conn.disconnect();
                    } catch (Throwable dummy) {
                    }
                }
            } finally {
                _end = new Date();
            }
        }
