    public static BtreeDictParameters read(String dir, URL hsBase) throws Exception {
        URL baseURL = null, url, tmapURL = null;
        URLConnection connect;
        BufferedReader in;
        File file;
        int blockSize = -1;
        int rootPosition = -1;
        int freeID = -1;
        if (hsBase == null) {
            file = new File(dir);
            if (file.exists()) {
                if (File.separatorChar != '/') {
                    dir = dir.replace(File.separatorChar, '/');
                }
                if (dir.lastIndexOf(File.separatorChar) != dir.length() - 1) {
                    dir = dir.concat(File.separator);
                }
                debug("file:" + dir);
                baseURL = new URL("file", "", dir);
            } else {
                baseURL = new URL(dir);
            }
        }
        if (hsBase != null) {
            url = new URL(hsBase, dir + "/SCHEMA");
        } else {
            url = new URL(baseURL, "SCHEMA");
        }
        connect = url.openConnection();
        in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
        String line;
        do {
            line = in.readLine();
        } while (!line.startsWith("TMAP"));
        in.close();
        StringTokenizer tokens = new StringTokenizer(line, " =");
        tokens.nextToken();
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            if (token.equals("bs")) blockSize = Integer.parseInt(tokens.nextToken()); else if (token.equals("rt")) rootPosition = Integer.parseInt(tokens.nextToken()); else if (token.equals("id1")) freeID = Integer.parseInt(tokens.nextToken());
        }
        if (hsBase != null) {
            tmapURL = new URL(hsBase, dir + "/TMAP");
        } else {
            tmapURL = new URL(baseURL, "TMAP");
        }
        BtreeDictParameters bdp = null;
        if (hsBase == null) {
            bdp.setDirName(Utilities.URLDecoder(baseURL.getFile()));
        }
        return bdp;
    }
