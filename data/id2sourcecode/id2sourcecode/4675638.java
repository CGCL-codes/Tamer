    private static void getURLContentByJDK(String s) throws Exception {
        log.log(Level.INFO, "进来获取URL");
        URL url = new URL(s);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "GBK"));
        char[] buffer = new char[256];
        int length = -1;
        while ((length = br.read(buffer)) != -1) {
            System.out.print(new String(buffer, 0, length));
        }
    }
