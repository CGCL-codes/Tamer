    static ArrayList<URL> loadScriptDirectory(URL directory) throws IOException {
        URLConnection urlConn = directory.openConnection();
        InputStreamReader directoryInput = new InputStreamReader(urlConn.getInputStream());
        BufferedReader directoryReader = new BufferedReader(directoryInput);
        ArrayList<URL> scriptURLs = new ArrayList<URL>();
        String line = null;
        while (true) {
            line = directoryReader.readLine();
            if (line == null) return scriptURLs;
            line = line.trim();
            if (line.isEmpty()) return scriptURLs;
            if (line.endsWith(".dex")) {
                URL dex = new URL(line);
                ArrayList<URL> moreScripts = loadScriptDirectory(dex);
                scriptURLs.addAll(moreScripts);
            } else if (line.endsWith(".xml")) {
                URL url = new URL(line);
                scriptURLs.add(url);
            } else throw new IOException("Dex \"" + directory + "\" contained bad line \"" + line + "\"");
        }
    }
