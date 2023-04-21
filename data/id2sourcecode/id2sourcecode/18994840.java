    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("usage: java OnlineStatus http://www.urltocheck.com [proxy proxyport]");
            System.out.println("gives as output: ");
            System.out.println("   online\n   offline\n   error");
        } else {
            try {
                URL url = new URL(args[0]);
                if (args.length == 3) {
                    System.setProperty("http.proxyHost", args[1]);
                    System.setProperty("http.proxyPort", args[2]);
                }
                InputStream is = url.openStream();
                System.out.println("online");
            } catch (IOException ex) {
                System.out.println("offline");
            }
        }
    }
