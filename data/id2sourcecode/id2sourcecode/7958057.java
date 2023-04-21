    private void initHTMLPane() {
        JTextPane htmlPane = new javax.swing.JTextPane();
        htmlPane.addHyperlinkListener(new Hyperactive());
        jScrollPane1.setViewportView(htmlPane);
        try {
            URL url = new URL("file:///E:/Project/web_sites/delormepro/index.html");
            HTMLEditorKit kit = new HTMLEditorKit();
            Document doc = kit.createDefaultDocument();
            HTMLDocument htmlDoc = (HTMLDocument) doc;
            htmlDoc.setBase(url);
            URLConnection conn = url.openConnection();
            String type = conn.getContentType();
            String enc = conn.getContentEncoding();
            InputStream in = conn.getInputStream();
            Reader reader = new InputStreamReader(in, "iso-8859-1");
            kit.read(in, htmlDoc, 0);
            Element root = htmlDoc.getDefaultRootElement();
            printDoc(htmlDoc);
            System.out.println(htmlDoc);
            htmlPane.setDocument(htmlDoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
