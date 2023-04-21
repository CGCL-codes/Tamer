                public void run() {
                    try {
                        URL burl;
                        burl = ((SVGOMDocument) document).getURLObject();
                        URL url;
                        if (burl != null) url = new URL(burl, uri); else url = new URL(uri);
                        final URLConnection conn = url.openConnection();
                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        conn.setUseCaches(false);
                        conn.setRequestProperty("Content-Type", mimeType);
                        OutputStream os = conn.getOutputStream();
                        String e = null, enc = fEnc;
                        if (enc != null) {
                            if (enc.startsWith(DEFLATE)) {
                                os = new DeflaterOutputStream(os);
                                if (enc.length() > DEFLATE.length()) enc = enc.substring(DEFLATE.length() + 1); else enc = "";
                                conn.setRequestProperty("Content-Encoding", DEFLATE);
                            }
                            if (enc.startsWith(GZIP)) {
                                os = new GZIPOutputStream(os);
                                if (enc.length() > GZIP.length()) enc = enc.substring(GZIP.length() + 1); else enc = "";
                                conn.setRequestProperty("Content-Encoding", DEFLATE);
                            }
                            if (enc.length() != 0) {
                                e = EncodingUtilities.javaEncoding(enc);
                                if (e == null) e = UTF_8;
                            } else {
                                e = UTF_8;
                            }
                        }
                        Writer w;
                        if (e == null) w = new OutputStreamWriter(os); else w = new OutputStreamWriter(os, e);
                        w.write(content);
                        w.flush();
                        w.close();
                        os.close();
                        InputStream is = conn.getInputStream();
                        Reader r;
                        e = UTF_8;
                        if (e == null) r = new InputStreamReader(is); else r = new InputStreamReader(is, e);
                        r = new BufferedReader(r);
                        final StringBuffer sb = new StringBuffer();
                        int read;
                        char[] buf = new char[4096];
                        while ((read = r.read(buf, 0, buf.length)) != -1) {
                            sb.append(buf, 0, read);
                        }
                        r.close();
                        updateRunnableQueue.invokeLater(new Runnable() {

                            public void run() {
                                try {
                                    h.getURLDone(true, conn.getContentType(), sb.toString());
                                } catch (Exception e) {
                                    if (userAgent != null) {
                                        userAgent.displayError(e);
                                    }
                                }
                            }
                        });
                    } catch (Exception e) {
                        if (e instanceof SecurityException) {
                            userAgent.displayError(e);
                        }
                        updateRunnableQueue.invokeLater(new Runnable() {

                            public void run() {
                                try {
                                    h.getURLDone(false, null, null);
                                } catch (Exception e) {
                                    if (userAgent != null) {
                                        userAgent.displayError(e);
                                    }
                                }
                            }
                        });
                    }
                }
