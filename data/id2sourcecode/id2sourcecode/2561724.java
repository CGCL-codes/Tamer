    public static Tree readNHtree(URL url) throws Exception {
        String incoming = "";
        StringBuffer sb = null;
        BufferedReader in = null;
        sb = new StringBuffer(10000);
        in = new BufferedReader(new InputStreamReader(url.openStream()));
        while ((incoming = in.readLine()) != null) {
            sb.append(incoming);
        }
        in.close();
        return new Tree(sb.toString());
    }
