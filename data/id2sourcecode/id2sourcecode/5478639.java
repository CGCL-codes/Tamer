    private static void initRoots() {
        String roots = System.getProperty("fconn.listenroots");
        if (roots != null) {
            StringTokenizer tokens = new StringTokenizer(roots, ";");
            listenRoots = new String[tokens.countTokens()];
            int i = 0;
            while (tokens.hasMoreTokens()) {
                listenRoots[i++] = tokens.nextToken();
            }
        } else {
            File[] fileroots = File.listRoots();
            listenRoots = new String[fileroots.length];
            for (int c = 0; c < fileroots.length; c++) {
                listenRoots[c] = fileroots[c].getAbsolutePath();
            }
            File filesystem = new File(System.getProperty("user.home"), "j2mewtk/2.5.2/appdb/DefaultColorPhone/filesystem");
            if (filesystem.exists() && filesystem.isDirectory()) {
                File[] files = filesystem.listFiles();
                Vector dirs = new Vector();
                for (int c = 0; c < files.length; c++) {
                    if (files[c].isDirectory()) {
                        String name = files[c].getName() + "/";
                        remap.put("file:///" + name, files[c].getAbsolutePath() + "/");
                        dirs.add(name);
                    }
                }
                String[] old = listenRoots;
                listenRoots = new String[old.length + dirs.size()];
                System.arraycopy(old, 0, listenRoots, 0, old.length);
                for (int c = old.length; c < listenRoots.length; c++) {
                    listenRoots[c] = (String) dirs.elementAt(c - old.length);
                }
            }
            for (int c = 0; c < listenRoots.length; c++) {
                listenRoots[c] = listenRoots[c].replaceAll("\\\\", "/");
            }
        }
    }
