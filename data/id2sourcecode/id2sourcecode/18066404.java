    public String getTextoUrl(URL url) {
        String texto = null;
        try {
            URLConnection con = url.openConnection();
            InputStream contenido = con.getInputStream();
            BufferedReader isr = new BufferedReader(new InputStreamReader(contenido));
            String linea = isr.readLine();
            while (null != linea) {
                texto += linea;
                linea = isr.readLine();
            }
            contenido.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return texto;
    }
