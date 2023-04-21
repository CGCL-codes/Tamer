    public java.util.List<File> getSnippets() {
        java.util.List<File> rtn = new ArrayList<File>();
        String s = getProperty(MERLOT_SNIPPETS_INFO);
        if (s != null) {
            File userSnippetsDir = createSnippetsDir();
            InputStream is = getClass().getResourceAsStream("/" + s);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    URL url = getClass().getResource("/" + line);
                    URLConnection connection = url.openConnection();
                    File cache = new File(userSnippetsDir, line);
                    File snippet = downloadSnippet(connection, cache);
                    if (snippet.exists()) {
                        rtn.add(snippet);
                    }
                }
                br.close();
                is.close();
            } catch (IOException e) {
                MerlotDebug.exception(e);
            }
        }
        return rtn;
    }
