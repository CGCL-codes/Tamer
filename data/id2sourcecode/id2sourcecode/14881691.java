    public Boolean consult(final Object[] r, final List bindings) throws Exception {
        String src;
        BufferedReader in;
        String XID = "";
        Object[] retobj = new Object[r.length];
        Object[] values = null;
        if (r.length == 2) {
            if (r[0] instanceof String) {
                retobj[0] = XID = (String) r[0];
            } else {
                retobj[0] = XID = prova.newXID();
            }
            src = XID;
            retobj[1] = r[1];
            StringReader sr = new StringReader(r[1].toString());
            in = new BufferedReader(sr);
        } else {
            retobj[0] = r[0];
            if (r[0] instanceof ComplexTerm) {
                String input = "";
                src = "";
                ComplexTerm cterm = (ComplexTerm) r[0];
                Term[] content = cterm.getTerms();
                if (content.length == 2) {
                    src = ((ConstantTerm) content[1]).getObject().toString();
                    Term[] adds = ((ComplexTerm) content[0]).getTerms();
                    String s = adds[1].toString();
                    input = s.substring(1, s.length() - 1);
                    Term[] binds = ((ComplexTerm) adds[0]).getTerms();
                    if (binds[1] instanceof ComplexTerm) {
                        List boundObjects = new ArrayList();
                        RListUtils.rlist2List((ComplexTerm) binds[1], boundObjects, 0);
                        for (int i = 0; i < boundObjects.size(); i++) {
                            Object bo = boundObjects.get(i);
                            if (bo instanceof ComplexTerm) input = input.replaceAll("_" + i, bo.toString());
                        }
                        values = boundObjects.toArray();
                    }
                } else throw new ParsingException("consult parameters are invalid");
                StringReader sr = new StringReader(input);
                in = new BufferedReader(sr);
            } else if (r[0] instanceof String) {
                src = (String) r[0];
                File file = new File(src);
                if (!file.exists() || !file.canRead()) {
                    try {
                        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(src);
                        in = new BufferedReader(new InputStreamReader(is));
                    } catch (Exception e) {
                        try {
                            URL url = new URL(src);
                            in = new BufferedReader(new InputStreamReader(url.openStream()));
                        } catch (Exception ex) {
                            throw new IOException();
                        }
                    }
                } else {
                    FileReader fr = new FileReader(file);
                    in = new BufferedReader(fr);
                }
            } else if (r[0] instanceof BufferedReader) {
                src = "BufferedReader";
                in = (BufferedReader) r[0];
            } else if (r[0] instanceof StringBuffer) {
                src = "StringBuffer";
                StringReader sr = new StringReader(((StringBuffer) r[0]).toString());
                in = new BufferedReader(sr);
            } else {
                throw new ParsingException("consult parameters are invalid");
            }
        }
        RulebaseParser parser = new RulebaseParser(this, src, values);
        AtomicConsult temp_kb = new AtomicConsult();
        parser.parse(temp_kb, in);
        bindings.add(retobj);
        prova.commit_consult(XID, src, temp_kb);
        return Boolean.TRUE;
    }
