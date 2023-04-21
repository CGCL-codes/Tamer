    public ArrayList<String[]> getFileList() throws Exception {
        ArrayList<String[]> ret = new ArrayList<String[]>();
        URL url = new URL(UPGRADE_LOCATION_URL);
        System.out.println("md5URL: " + url);
        BufferedReader in = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int status = conn.getResponseCode();
            if (status == 200) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while (true) {
                    String s = in.readLine();
                    if (s == null) break;
                    System.out.println(s);
                    int pos = s.indexOf(DELIM);
                    if (pos >= 0) {
                        int npos = s.indexOf(DELIM, pos + 1);
                        if (npos < 0) continue;
                        String name = s.substring(0, pos);
                        String loc = s.substring(pos + 1, npos);
                        String md5 = s.substring(npos + 1);
                        System.out.println("*" + name + ":" + loc + ":" + md5);
                        String line[] = { name, loc, md5 };
                        ret.add(line);
                    }
                }
            }
            return ret;
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
