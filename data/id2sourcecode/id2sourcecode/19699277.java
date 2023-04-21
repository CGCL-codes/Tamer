    public static void main(String[] args) {
        LinkChecker checker = new LinkChecker();
        for (int i = 0; i < args.length; i++) {
            URL url;
            InputStream stream;
            try {
                url = new URL(args[i]);
                stream = url.openStream();
            } catch (IOException e) {
                try {
                    File f = new File(args[i]);
                    stream = new FileInputStream(f);
                    url = f.toURL();
                } catch (IOException f) {
                    System.err.println("Failed to open URL: " + args[i]);
                    continue;
                }
            }
            checker.addURL(url, stream);
            checker.parse();
        }
    }
