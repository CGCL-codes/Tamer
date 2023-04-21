                public void run() {
                    try {
                        URL burl;
                        burl = ((SVGOMDocument) document).getURLObject();
                        final ParsedURL purl = new ParsedURL(burl, uri);
                        String e = null;
                        if (enc != null) {
                            e = EncodingUtilities.javaEncoding(enc);
                            e = ((e == null) ? enc : e);
                        }
                        InputStream is = purl.openStream();
                        Reader r;
                        if (e == null) {
                            r = new InputStreamReader(is);
                        } else {
                            try {
                                r = new InputStreamReader(is, e);
                            } catch (UnsupportedEncodingException uee) {
                                r = new InputStreamReader(is);
                            }
                        }
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
                                    h.getURLDone(true, purl.getContentType(), sb.toString());
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
