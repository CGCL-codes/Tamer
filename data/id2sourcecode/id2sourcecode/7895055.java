    public static void main(String[] args) {
        if (args.length == 0) usage();
        try {
            System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
            URL url = new URL(args[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (args.length > 1 && args[1].equals("-n")) connection.setFollowRedirects(false);
            connection.connect();
            for (int i = 0; connection.getHeaderField(i) != null; i++) System.err.println("Field " + i + ": " + connection.getHeaderFieldKey(i) + " / " + connection.getHeaderField(i));
            System.out.println(FormContentSearcher.getURLContents(new URL(args[0])));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
