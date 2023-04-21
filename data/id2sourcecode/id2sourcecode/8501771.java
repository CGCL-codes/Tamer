    private static String[] getJarFilesFromDir(File dir) {
        if (dir.isDirectory()) {
            Vector files = new Vector();
            String s[] = dir.list();
            for (int i = 0; i < s.length; i++) {
                if (s[i].endsWith(".jar")) {
                    files.add(s[i]);
                }
            }
            String s2[] = new String[files.size()];
            files.copyInto(s2);
            s = null;
            files = null;
            return s2;
        }
        Log.warning("Path \"" + dir + "\" is not a valid directory");
        return new String[0];
    }
