    public Corpus() throws IOException {
        HTML.Tag unTag = HTML.Tag.A;
        int cptPages = 1, cptLiens, nbBoucles = 1100;
        URL url;
        URLConnection connection;
        InputStream is;
        InputStreamReader isr;
        BufferedReader br;
        CharSequence s = "_ylu";
        CharSequence extPDF = "pdf", extDOC = "doc", extPPT = "ppt";
        HTMLEditorKit htmlKit;
        HTMLDocument htmlDoc;
        String themes[] = { "entertainment" };
        String lien;
        for (int k = 0; k < themes.length; k++) {
            cptLiens = 0;
            while (cptLiens < nbBoucles) {
                url = new URL("http://search.yahoo.com/search?p=" + themes[k] + "&ei=UTF-8&fr=yfp-t-501&fp_ip=FR&pstart=1&b=" + cptPages + "");
                try {
                    connection = url.openConnection();
                    is = connection.getInputStream();
                    isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);
                    htmlKit = new HTMLEditorKit();
                    htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();
                    HTMLEditorKit.Parser parser = new ParserDelegator();
                    HTMLEditorKit.ParserCallback callback = htmlDoc.getReader(0);
                    parser.parse(br, callback, true);
                    htmlDoc.setBase(url);
                    HTMLDocument.Iterator htmlIT = htmlDoc.getIterator(unTag);
                    for (; (htmlIT != null) && htmlIT.isValid(); htmlIT.next()) {
                        AttributeSet attrSet = htmlIT.getAttributes();
                        for (Enumeration<HTML.Attribute> e = (Enumeration<Attribute>) attrSet.getAttributeNames(); e.hasMoreElements(); ) {
                            HTML.Attribute htmlAttr = (HTML.Attribute) e.nextElement();
                            lien = ((String) attrSet.getAttribute(htmlAttr));
                            if (lien.contains(s) && !(lien.contains(extPDF) || lien.contains(extDOC) || lien.contains(extPPT)) && cptLiens < 1000) {
                                cptLiens++;
                                if ((cptLiens % 10 == 0) || (cptLiens == 1)) System.out.println(themes[k] + " : " + cptLiens + "/" + nbBoucles);
                                ThreadCollect thc = new ThreadCollect(themes[k], lien, cptLiens);
                                thc.start();
                            }
                        }
                    }
                    cptPages += 10;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            k++;
        }
    }
