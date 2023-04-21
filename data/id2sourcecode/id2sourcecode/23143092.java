    public boolean renderURL(String urlstring, OutputStream out, short type) throws IOException, SAXException {
        if (!urlstring.startsWith("http:") && !urlstring.startsWith("ftp:") && !urlstring.startsWith("file:")) urlstring = "http://" + urlstring;
        URL url = new URL(urlstring);
        URLConnection con = url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; Transformer/2.x; Linux) CSSBox/2.x (like Gecko)");
        InputStream is = con.getInputStream();
        DOMSource parser = new DOMSource(is);
        parser.setContentType(con.getHeaderField("Content-Type"));
        Document doc = parser.parse();
        DOMAnalyzer da = new DOMAnalyzer(doc, url);
        da.attributesToStyles();
        da.addStyleSheet(null, CSSNorm.stdStyleSheet());
        da.addStyleSheet(null, CSSNorm.userStyleSheet());
        da.getStyleSheets();
        is.close();
        if (type == TYPE_PNG) {
            ReplacedImage.setLoadImages(true);
            BrowserCanvas contentCanvas = new BrowserCanvas(da.getRoot(), da, new java.awt.Dimension(1000, 600), url);
            ImageIO.write(contentCanvas.getImage(), "png", out);
        } else if (type == TYPE_SVG) {
            ReplacedImage.setLoadImages(true);
            BrowserCanvas contentCanvas = new BrowserCanvas(da.getRoot(), da, new java.awt.Dimension(1000, 600), url);
            PrintWriter w = new PrintWriter(new OutputStreamWriter(out, "utf-8"));
            writeSVG(contentCanvas.getViewport(), w);
            w.close();
        }
        return true;
    }
