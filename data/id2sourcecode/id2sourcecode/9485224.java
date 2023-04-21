    private void loadPage_cssbox(String adr) {
        InputStream is = null;
        try {
            URL url = new URL(adr);
            URLConnection con = url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; BoxBrowserTest/2.x; Linux) CSSBox/2.x (like Gecko)");
            is = con.getInputStream();
            DOMSource parser = new DOMSource(is);
            parser.setContentType(con.getHeaderField("Content-Type"));
            Document doc = parser.parse();
            DOMAnalyzer da = new DOMAnalyzer(doc, url);
            da.attributesToStyles();
            da.addStyleSheet(null, CSSNorm.stdStyleSheet());
            da.addStyleSheet(null, CSSNorm.userStyleSheet());
            da.getStyleSheets();
            cssbox = new BrowserCanvas(da.getRoot(), da, contentScroll.getSize(), url);
            contentScroll.setViewportView(cssbox);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
