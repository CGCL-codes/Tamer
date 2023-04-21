    public static void convert(Reader reader, Writer writer) {
        try {
            XPath parser = new XPath(reader);
            SimpleNode tree = parser.XPath2();
            if (null == tree) printErrorString(writer, "no data"); else {
                PrintWriter ps = new PrintWriter(writer);
                convert("", ps, tree);
            }
        } catch (Exception e) {
            printErrorString(writer, e.getMessage());
        } catch (Error err) {
            printErrorString(writer, err.getMessage());
        }
    }
