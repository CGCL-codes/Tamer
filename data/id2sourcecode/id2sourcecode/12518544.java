    private String obtenerComuId(String com) throws IOException {
        String nextLine = "";
        URL url = null;
        URLConnection urlConn = null;
        InputStreamReader inStream = null;
        BufferedReader buff = null;
        url = new URL("http://www.taringa.net/" + this.getUsuario() + "com");
        System.out.println("http://www.taringa.net/" + this.getUsuario() + "com");
        System.out.println("obteniendo ID de las comunidades de " + this.getUsuario());
        urlConn = url.openConnection();
        inStream = new InputStreamReader(urlConn.getInputStream());
        buff = new BufferedReader(inStream);
        while (true) {
            nextLine = buff.readLine();
            if (nextLine != null) {
                System.out.println(nextLine);
                if (nextLine.contains("comid:")) {
                }
            } else System.out.println("nulo");
        }
    }
