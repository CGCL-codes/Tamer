    public static String read(URL url) throws IOException {
        if (url == null) return null;
        InputStreamReader isr = new InputStreamReader(url.openStream(), encoding);
        try {
            String ret = "";
            BufferedReader stdin = new BufferedReader(isr);
            String str = stdin.readLine();
            while (str != null) {
                ret = ret + str + "\n";
                str = stdin.readLine();
            }
            return ret;
        } finally {
            isr.close();
        }
    }
