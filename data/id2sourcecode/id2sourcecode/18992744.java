    public String enviaPOST() {
        String respuesta = "";
        try {
            URL url = new URL(getProtocolo(), getHost(), getRuta());
            URLConnection urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConn.setRequestProperty("USER-AGENT", getIdentificacionNavegador());
            DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());
            String content = this.getParametrosPOST();
            printout.writeBytes(content);
            printout.flush();
            printout.close();
            BufferedReader input = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String str;
            while (null != ((str = input.readLine()))) {
                respuesta = respuesta + str;
            }
            input.close();
        } catch (MalformedURLException me) {
            log.severe("URL malformada: " + me.getMessage());
        } catch (IOException ioe) {
            log.severe("Excepcion de IO " + ioe.getMessage());
        }
        return respuesta;
    }
