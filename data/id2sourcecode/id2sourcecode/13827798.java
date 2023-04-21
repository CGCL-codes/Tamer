        public void run() {
            URL myurl = cacheUrl;
            String mypost = postData;
            if (mypost != null) {
                try {
                    mypost = Utils.replaceAll("${alarm}", URLEncoder.encode(alarm, "UTF-8"), mypost);
                    mypost = Utils.replaceAll("${source}", URLEncoder.encode(source, "UTF-8"), mypost);
                } catch (UnsupportedEncodingException ex) {
                    log.error("Cannot encode alarm or source in POST data", ex);
                    return;
                }
            }
            if (myurl == null) {
                try {
                    String _url = Utils.replaceAll("${alarm}", URLEncoder.encode(alarm, "UTF-8"), url);
                    _url = Utils.replaceAll("${source}", URLEncoder.encode(source, "UTF-8"), _url);
                    myurl = new URL(_url);
                } catch (MalformedURLException ex) {
                    log.error("Resulting URL is invalid", ex);
                } catch (UnsupportedEncodingException ex) {
                    log.error("Cannot encode alarm or source in URL", ex);
                }
            }
            if (myurl != null) {
                try {
                    InputStream ins = null;
                    if (mypost == null) {
                        ins = myurl.openStream();
                    } else {
                        URLConnection conn = myurl.openConnection();
                        conn.setDoOutput(true);
                        if (conn instanceof HttpURLConnection) {
                            ((HttpURLConnection) conn).setRequestMethod("POST");
                            conn.addRequestProperty("Content-Length", Integer.toString(mypost.length()));
                        }
                        OutputStream outs = conn.getOutputStream();
                        outs.write(mypost.getBytes());
                        outs.flush();
                        ins = conn.getInputStream();
                    }
                    StringBuilder sb = new StringBuilder(4096);
                    if (expResp != null) {
                        byte[] buf = new byte[1024];
                        int leidos = ins.read(buf);
                        while (leidos > 0) {
                            String _r = new String(buf, 0, leidos);
                            sb.append(_r);
                            if (sb.length() >= 4096) {
                                leidos = -1;
                            } else {
                                leidos = ins.read(buf);
                            }
                        }
                        if (sb.indexOf(expResp) < 0) {
                            log.warn("Did not get expected response sending alarm over URL {}", myurl);
                        }
                    }
                    ins.close();
                } catch (IOException ex) {
                    log.error("Sending alarm over URL", ex);
                }
            }
        }
