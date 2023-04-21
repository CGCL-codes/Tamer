    public String obtenerURL(String direccion) throws Exception {
        InputStream is = null;
        try {
            URL url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() / 100 != 2) {
                throw new Exception("Código de respuesta inválido: " + connection.getResponseCode() + ", URL: " + direccion);
            }
            is = connection.getInputStream();
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(is));
            String linea;
            while ((linea = reader.readLine()) != null) {
                logger.log(Level.DEBUG, "Linea original: " + linea);
                linea = convertir(linea);
                logger.log(Level.DEBUG, "Llamada a extraer: " + linea);
                ArrayList<String> direcciones = UtilRegexp.extraeURLPDFs(linea, regexpURLsInteriores, 1);
                logger.log(Level.DEBUG, "Fin de extraer");
                if (direcciones.size() > 0) {
                    return prefijoInteriores + direcciones.get(0);
                }
            }
            throw new Exception("No encontrada URL al PDF del BOE");
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
