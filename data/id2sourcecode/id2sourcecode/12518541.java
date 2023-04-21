    private ArrayList<String[]> obtenerComunidades(String usuario) {
        ArrayList<String[]> value = new ArrayList<String[]>();
        String nextLine, comu = "";
        URL url = null;
        URLConnection urlConn = null;
        InputStreamReader inStream = null;
        BufferedReader buff = null;
        try {
            url = new URL("http://www.taringa.net/" + usuario + "/comunidades");
            System.out.println("obteniendo comunidades de " + usuario);
            urlConn = url.openConnection();
            inStream = new InputStreamReader(urlConn.getInputStream());
            buff = new BufferedReader(inStream);
            while (true) {
                nextLine = buff.readLine();
                if (nextLine != null) {
                    if (nextLine.contains("<div class=\"listado-content clearfix list-element\">")) {
                        while (!nextLine.contains("<div id=\"sidebar\">")) {
                            comu = comu + nextLine;
                            nextLine = buff.readLine();
                        }
                        break;
                    }
                } else {
                    break;
                }
            }
            value = refinar(comu);
        } catch (MalformedURLException e) {
            System.out.println("Please check the URL:" + e.toString());
        } catch (IOException e1) {
            System.out.println("Can't read  from the Internet: " + e1.toString());
        }
        return value;
    }
