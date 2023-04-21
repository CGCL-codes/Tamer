    public static void Send(String txt) {
        try {
            URL url = new URL(getURL(txt));
            InputStream in = url.openStream();
            BufferedInputStream bufIn = new BufferedInputStream(in);
            for (; ; ) {
                int data = bufIn.read();
                if (data == -1) break;
            }
        } catch (Exception e) {
        }
    }
