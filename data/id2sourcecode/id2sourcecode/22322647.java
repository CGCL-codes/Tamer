    public static void main(String[] argv) throws Exception {
        HtmlParser hp = new HtmlParser();
        java.net.URL url = new java.net.URL("http://localhost/wiki/index.php?title=LatestNews&printable=yes");
        Node yuzz = hp.parse(new InputSource(url.openStream()));
        PrintWriter out = new PrintWriter(System.out);
        out.println(yuzz.writeString());
        out.flush();
        out.close();
    }
