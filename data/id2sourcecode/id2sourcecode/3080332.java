    public void update() {
        try {
            System.out.println("url");
            URL url = new URL("https", HOST, "/sanford/Login.asp");
            System.out.println("connection");
            URLConnection connection = url.openConnection();
            System.out.println("content");
            Object content = connection.getContent();
            System.out.println("Content: ");
            System.out.println(content);
            System.out.println("done");
        } catch (java.io.IOException io) {
            System.out.println("io exception" + io);
        }
    }
