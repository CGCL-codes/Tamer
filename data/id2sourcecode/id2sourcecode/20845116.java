    public static void main(final String[] args) throws Exception {
        String[][] oArr = new String[][] { { "12.148.163.152", "8165" }, { "199.105.112.162", "8130" } };
        final List<Proxy> proxyList = new ArrayList<Proxy>(oArr.length);
        for (String[] arr : oArr) {
            String host = arr[0], port = arr[1];
            Proxy p = new Proxy(Type.HTTP, new InetSocketAddress(host, Integer.parseInt(port)));
            proxyList.add(p);
        }
        ProxySelector ps = new ProxySelector() {

            private List<Proxy> proxyList;

            @Override
            public List<Proxy> select(URI uri) {
                System.out.println(uri.getScheme());
                if (uri.getScheme().equals("http")) {
                    return proxyList;
                }
                return null;
            }

            @Override
            public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
            }
        };
        ProxySelector.setDefault(ps);
        URL url = new URL("http://www.google.com");
        URLConnection conn = url.openConnection();
        if (conn != null) {
            InputStream in = conn.getInputStream();
            if (in != null) {
                int i = -1;
                while ((i = in.read()) != -1) {
                    System.out.print(i);
                }
            }
        }
    }
