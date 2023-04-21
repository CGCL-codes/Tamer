    public static void main(String[] args) {
        try {
            URL url = new URL("http://jj.24365pt.com/index.jhtml");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            InputStream in = null;
            in = url.openStream();
            String content = pipe(in, "utf-8");
            System.out.println(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
