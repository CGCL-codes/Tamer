    protected static List getDefaultBrowsers() {
        if (defaultBrowsers2 != null) return defaultBrowsers2;
        Reader reader = null;
        defaultBrowsers2 = new ArrayList();
        try {
            URL url = WebBrowserUIPlugin.getInstance().getBundle().getEntry("defaultBrowsers.xml");
            URL url2 = Platform.resolve(url);
            reader = new InputStreamReader(url2.openStream());
            IMemento memento = XMLMemento.createReadRoot(reader);
            IMemento[] children = memento.getChildren("browser");
            if (children != null) {
                int size = children.length;
                for (int i = 0; i < size; i++) {
                    IMemento child = children[i];
                    String name = child.getString("name");
                    String executable = child.getString("executable");
                    String params = child.getString("params");
                    List locations = new ArrayList();
                    IMemento[] locat = child.getChildren("location");
                    if (locat != null) {
                        int size2 = locat.length;
                        for (int j = 0; j < size2; j++) locations.add(locat[j].getTextData());
                    }
                    String[] loc = new String[locations.size()];
                    locations.toArray(loc);
                    DefaultBrowser db = new DefaultBrowser(name, executable, params, loc);
                    Trace.trace(Trace.CONFIG, "Default browser: " + db);
                    defaultBrowsers2.add(db);
                }
            }
        } catch (Exception e) {
            Trace.trace(Trace.SEVERE, "Error loading default browsers", e);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
        return defaultBrowsers2;
    }
