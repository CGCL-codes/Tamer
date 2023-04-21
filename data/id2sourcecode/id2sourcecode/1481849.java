    public static void createTestResult(OutputFormat formatter, URL url) {
        Reader sourceReader = null;
        Writer targetWriter = null;
        try {
            sourceReader = new InputStreamReader(url.openStream(), "UTF-8");
            String fullPath = url.getPath();
            String name = fullPath.substring(fullPath.lastIndexOf("/") + 1);
            File f = new File("c:/_test/" + name + ".html");
            f.createNewFile();
            targetWriter = new FileWriter(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        formatter.format(sourceReader, targetWriter);
    }
