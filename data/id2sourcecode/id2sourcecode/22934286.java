    public String getDefinition(String text) {
        String def = "";
        try {
            URL url = new URL("http://en.wiktionary.org/wiki/" + sanitize(text));
            URLConnection connection = url.openConnection();
            DataInputStream in = new DataInputStream(connection.getInputStream());
            String line;
            Pattern defPattern = Pattern.compile("<li>(.+)</li>");
            Boolean inDef = false;
            Boolean inDefItem = false;
            while ((line = in.readLine()) != null) {
                if (line.indexOf("<ol>") >= 0) {
                    inDef = true;
                }
                if (inDef) {
                    if (line.indexOf("<li>") >= 0) {
                        inDefItem = true;
                    }
                    if (inDefItem) {
                        def += line.replaceAll("\\<.*?>", "");
                    }
                    if (line.indexOf("</li>") >= 0) {
                        if (inDefItem) {
                            def += ".\n";
                        }
                        inDefItem = false;
                    }
                }
                if (line.indexOf("</ol>") >= 0) {
                    inDef = false;
                }
            }
            in.close();
        } catch (Exception e) {
        }
        return def;
    }
