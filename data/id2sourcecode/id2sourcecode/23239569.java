    public void doDownload() throws Exception {
        if (getUrl() == null) {
            throw new IllegalStateException("No URL set!");
        }
        final URL url = new URL(getUrl());
        final Scanner scanner = new Scanner(url.openStream());
        scanner.useDelimiter(System.getProperty("line.separator"));
        final StringBuilder xml_content = new StringBuilder();
        while (scanner.hasNext()) {
            String s = scanner.next();
            xml_content.append(s);
            xml_content.append("\n");
        }
        scanner.close();
        oPageContent = xml_content.toString();
    }
