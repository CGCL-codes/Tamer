    public void generate() throws IOException {
        generate(input, rootOutput);
        FileUtils.copyFile(getClass().getResourceAsStream(Config.getInstance().getProperty("cssStyles", "/styles.css")), new File(rootOutput, "sourcetohtml-styles.css"));
    }
