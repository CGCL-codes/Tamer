    public static void main(String[] args) throws IOException {
        System.setProperty("http.proxyHost", "cornillon.grenoble.xrce.xerox.com");
        System.setProperty("http.proxyPort", "8000");
        System.out.println("attempting connection through proxy...");
        URL url = new URL("http://iserve.kmi.open.ac.uk/resource/services/dfcd3e9e-4684-46bd-acc6-4d95c1ff46cb");
        InputStream in = url.openStream();
        System.out.println("connection done.");
    }
