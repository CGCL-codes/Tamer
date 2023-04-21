    public void doDownload() throws Exception {
        if (oWikiUrl == null || oTargetDir == null) {
            throw new IllegalStateException("One ore more arguments are not set correctly!");
        }
        for (String page : oPageList) {
            final File file = new File(oTargetDir, page + "." + "xml").getAbsoluteFile();
            oFileList.add(file);
            final URL url = new URL(oWikiUrl.replace("PAGE", page));
            final Scanner scanner = new Scanner(url.openStream());
            scanner.useDelimiter(System.getProperty("line.separator"));
            final StringBuilder xml_content = new StringBuilder();
            while (scanner.hasNext()) {
                String s = scanner.next();
                xml_content.append(s);
                xml_content.append("\n");
            }
            scanner.close();
            final FileWriter fw = new FileWriter(file);
            fw.write(xml_content.toString());
            fw.flush();
            fw.close();
        }
    }
