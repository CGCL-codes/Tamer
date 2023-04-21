    public List<MLetContent> parse(URL url) throws IOException {
        String mth = "parse";
        String requiresTypeWarning = "<arg type=... value=...> tag requires type parameter.";
        String requiresValueWarning = "<arg type=... value=...> tag requires value parameter.";
        String paramOutsideWarning = "<arg> tag outside <mlet> ... </mlet>.";
        String requiresCodeWarning = "<mlet> tag requires either code or object parameter.";
        String requiresJarsWarning = "<mlet> tag requires archive parameter.";
        URLConnection conn;
        conn = url.openConnection();
        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        url = conn.getURL();
        List<MLetContent> mlets = new ArrayList<MLetContent>();
        Map<String, String> atts = null;
        List<String> types = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        while (true) {
            c = in.read();
            if (c == -1) break;
            if (c == '<') {
                c = in.read();
                if (c == '/') {
                    c = in.read();
                    String nm = scanIdentifier(in);
                    if (c != '>') throw new IOException("Missing '>' in tag");
                    if (nm.equalsIgnoreCase(tag)) {
                        if (atts != null) {
                            mlets.add(new MLetContent(url, atts, types, values));
                        }
                        atts = null;
                        types = new ArrayList<String>();
                        values = new ArrayList<String>();
                    }
                } else {
                    String nm = scanIdentifier(in);
                    if (nm.equalsIgnoreCase("arg")) {
                        Map<String, String> t = scanTag(in);
                        String att = t.get("type");
                        if (att == null) {
                            if (isTraceOn()) {
                                trace(mth, requiresTypeWarning);
                            }
                            throw new IOException(requiresTypeWarning);
                        } else {
                            if (atts != null) {
                                types.add(att);
                            } else {
                                if (isTraceOn()) {
                                    trace(mth, paramOutsideWarning);
                                }
                                throw new IOException(paramOutsideWarning);
                            }
                        }
                        String val = t.get("value");
                        if (val == null) {
                            if (isTraceOn()) {
                                trace(mth, requiresValueWarning);
                            }
                            throw new IOException(requiresValueWarning);
                        } else {
                            if (atts != null) {
                                values.add(val);
                            } else {
                                if (isTraceOn()) {
                                    trace(mth, paramOutsideWarning);
                                }
                                throw new IOException(paramOutsideWarning);
                            }
                        }
                    } else {
                        if (nm.equalsIgnoreCase(tag)) {
                            atts = scanTag(in);
                            if (atts.get("code") == null && atts.get("object") == null) {
                                if (isTraceOn()) {
                                    trace(mth, requiresCodeWarning);
                                }
                                atts = null;
                                throw new IOException(requiresCodeWarning);
                            }
                            if (atts.get("archive") == null) {
                                if (isTraceOn()) {
                                    trace(mth, requiresJarsWarning);
                                }
                                atts = null;
                                throw new IOException(requiresJarsWarning);
                            }
                        }
                    }
                }
            }
        }
        in.close();
        return mlets;
    }
