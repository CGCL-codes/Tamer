    public static void main(String args[]) {
        if (args.length != 1) {
            System.err.println("Usage: SimpleBrowser <url>");
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
            SimpleBrowser test = new SimpleBrowser(da.getRoot(), url, da);
            test.setSize(1275, 750);
            test.setVisible(true);
            is.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
