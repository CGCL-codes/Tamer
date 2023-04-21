    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: TextBoxes <url>");
            System.exit(0);
        }
        try {
            URL url = new URL(args[0]);
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            DOMSource parser = new DOMSource(is);
            Document doc = parser.parse();
            DOMAnalyzer da = new DOMAnalyzer(doc, url);
            da.attributesToStyles();
            da.addStyleSheet(null, CSSNorm.stdStyleSheet());
            da.addStyleSheet(null, CSSNorm.userStyleSheet());
            da.getStyleSheets();
            ReplacedImage.setLoadImages(false);
            BrowserCanvas browser = new BrowserCanvas(da.getRoot(), da, new java.awt.Dimension(1000, 600), url);
            printTextBoxes(browser.getViewport());
            is.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
