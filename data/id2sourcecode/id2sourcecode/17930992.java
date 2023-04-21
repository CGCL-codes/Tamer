    public static void main(String[] args) throws Exception {
        Partner partner = new Partner("456");
        User user = new User("123");
        String urlhttp = "https://server-ebics.webank.fr:28103/WbkPortalFileTransfert/EbicsProtocol";
        String E001Digest = "9BF804AF2B121A5B94C82BFD8E406FFB18024D3D4BF9E";
        String X001Digest = "9BF804AF2B121A5B94C82BFD8E406FFB18024D3D4BF9E";
        String hostId = "EBIXQUAL";
        InputStream inStream = new FileInputStream("ServerEbicsValerian.cer");
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(inStream);
        inStream.close();
        URL url = new URL(urlhttp);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setHostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {
                System.out.println(hostname);
                return true;
            }
        });
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
        connection.setRequestProperty("Accept", "text/xml");
        byte[] query = getQuery();
        int queryLength = query.length;
        connection.setRequestProperty("Content-length", String.valueOf(queryLength));
        OutputStream out = connection.getOutputStream();
        out.write(query);
        System.out.println("----------RESPONSE");
        System.out.println("Resp Code:" + connection.getResponseCode());
        System.out.println("Resp Message:" + connection.getResponseMessage());
        InputStream in = connection.getInputStream();
        FileOutputStream fOut = new FileOutputStream("out.xml");
        int ch = 0;
        while ((ch = in.read()) >= 0) {
            System.out.print((char) ch);
            fOut.write(ch);
        }
        fOut.close();
    }
