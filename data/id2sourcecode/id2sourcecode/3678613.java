    public static void main(String[] args) throws IOException {
        URL url = new URL("http://www.hust.edu.cn");
        System.out.println("Authority = " + url.getAuthority());
        System.out.println("Default port = " + url.getDefaultPort());
        System.out.println("File = " + url.getFile());
        System.out.println("Host = " + url.getHost());
        System.out.println("Path = " + url.getPath());
        System.out.println("Port = " + url.getPort());
        System.out.println("Protocol = " + url.getProtocol());
        System.out.println("Query = " + url.getQuery());
        System.out.println("Ref = " + url.getRef());
        System.out.println("User Info = " + url.getUserInfo());
        System.out.print('\n');
        InputStream is = url.openStream();
        int ch;
        is.close();
        InetAddress ia = InetAddress.getByName("www.sina.com.cn");
        System.out.println("Canonical Host Name = " + ia.getCanonicalHostName());
        System.out.println("Host Address = " + ia.getHostAddress());
        System.out.println("Host Name = " + ia.getHostName());
        System.out.println("Is Loopback Address = " + ia.isLoopbackAddress());
    }
