    private Help(String filename) throws IOException {
        super(filename);
        pageType = PageType.HELP;
        windowTitle = (CONF.windowtitle.length() > 0) ? CONF.windowtitle : CONF.propertyText("Help");
        printXhtmlHeader();
        InputStreamReader stream;
        if (!CONF.helpfile.equals("")) stream = new InputStreamReader(new FileInputStream(CONF.helpfile)); else stream = new InputStreamReader(AbstractPageWriter.class.getResourceAsStream("resources/help" + CONF.ext));
        char[] buf = new char[Doclet.BUFFER_SIZE];
        int n;
        while ((n = stream.read(buf)) > 0) write(buf, 0, n);
        stream.close();
        println();
        printXhtmlFooter();
        this.close();
    }
