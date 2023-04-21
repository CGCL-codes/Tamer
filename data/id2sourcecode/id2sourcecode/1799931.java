    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: ComputeStyles <url> <output_file>");
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
            System.err.println("Computing style...");
            da.stylesToDomInherited();
            PrintStream os = new PrintStream(new FileOutputStream(args[1]));
            Output out = new NormalOutput(doc);
            out.dumpTo(os);
            os.close();
            is.close();
            System.err.println("Done.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
