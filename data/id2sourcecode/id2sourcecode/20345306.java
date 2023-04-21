    public static void main(String[] args) throws Exception {
        System.out.println("Sent HTTP GET request to query customer info");
        URL url = new URL("http://localhost:8080/xml/customers");
        InputStream in = url.openStream();
        System.out.println(getStringFromInputStream(in));
    }
