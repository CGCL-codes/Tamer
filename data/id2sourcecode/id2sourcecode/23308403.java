    private static void load(String path, HashMap set) throws IOException, MalformedURLException {
        BufferedReader buffReader = null;
        if (path.indexOf("://") != -1) {
            URL url = new URL(path);
            String encoding = Toolkit.getDeclaredXMLEncoding(url.openStream());
            buffReader = new BufferedReader(new InputStreamReader(url.openStream(), encoding));
        } else {
            File toRead = new File(path);
            if (!toRead.exists()) {
                Toolkit.checkOrCreate(path, "targets data file");
                TargetWriter.rewriteTargets(new HashMap(), toRead);
            }
            String encoding = Toolkit.getDeclaredXMLEncoding(new FileInputStream(path));
            buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), encoding));
        }
        if (buffReader != null) {
            new TargetsReader(path, buffReader, new TargetsReaderListener(set)).read();
            buffReader.close();
        } else {
            throw new IOException("I/O error trying to read \"" + path + "\".");
        }
    }
