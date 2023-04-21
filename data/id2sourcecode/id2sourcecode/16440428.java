    public String toString(Tag tag) {
        String text = tag.text().trim();
        if (mapping == null) {
            mapping = new Properties();
            InputStream in = null;
            try {
                URL url;
                try {
                    url = new URL(System.getProperty("nativetaglet.mapping", "file:native-taglet.properties"));
                } catch (MalformedURLException e) {
                    url = new URL("file:" + System.getProperty("nativetaglet.mapping", "file:native-taglet.properties"));
                }
                in = url.openStream();
                mapping.load(in);
            } catch (Exception e) {
                System.err.println("[NATIVE TAGLET] Could not read mapping file");
                System.err.println("-->");
                e.printStackTrace(System.err);
                System.err.println("<--");
                System.err.println("[NATIVE TAGLET] !!! NO LINKS WILL BE GENERATED !!!");
            } finally {
                if (in != null) try {
                    in.close();
                } catch (Exception ignore) {
                }
            }
        }
        if (mapping != null) {
            String url = mapping.getProperty(text);
            if (url == null) {
                for (Iterator i = mapping.keySet().iterator(); i.hasNext(); ) {
                    String name = (String) i.next();
                    if (hasOpenGLSuffix(text, name)) {
                        url = mapping.getProperty(name);
                        break;
                    }
                }
            }
            if (url != null) {
                url = mapping.getProperty("nativetaglet.baseUrl", "") + url;
                text = "<a href=\"" + url + "\">" + text + "</a>";
            }
        }
        return text;
    }
