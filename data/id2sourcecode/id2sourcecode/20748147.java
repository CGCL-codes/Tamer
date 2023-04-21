    public synchronized void setMenus() {
        disposeMenus();
        String[] menuFiles = context.config().menus.split(";");
        ArrayList<MenuItem> allMenus = new ArrayList<MenuItem>();
        for (String menuFile : menuFiles) {
            if (menuFile.trim().length() == 0) continue;
            if (context.config().debug) System.out.println("PhyloUI setMenus(): " + menuFile);
            menuFile.replaceAll("'", "");
            menuFile.replaceAll("\"", "");
            PhyloMenuIO io = new PhyloMenuIO();
            Reader r = null;
            InputStream in = null;
            Exception asdf = null;
            if (menuFile.toLowerCase().startsWith("http")) {
                try {
                    URL url = new URL(menuFile);
                    in = url.openStream();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    in = p.createInput("menus/" + menuFile);
                    if (in == null) {
                        in = p.createInput(menuFile);
                    }
                    if (in == null) {
                        String path = p.getDocumentBase().toString();
                        int ind = path.lastIndexOf("/");
                        if (ind != -1) path = path.substring(0, ind);
                        in = p.createInput(path + "/" + menuFile);
                    }
                } catch (Exception e) {
                    asdf = e;
                }
            }
            if (in == null) {
                r = new StringReader(menuFile);
            } else {
                r = new InputStreamReader(in);
            }
            if (in == null) {
                asdf.printStackTrace();
            }
            ArrayList<MenuItem> theseMenus = io.loadFromXML(r, p, context.ui(), p, context.config());
            configureMenus(theseMenus);
            allMenus.addAll(theseMenus);
        }
        this.menus = allMenus;
        if (context.config().debug) System.out.println("Finished!");
    }
