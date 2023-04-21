    public static boolean descargarFichero(String strURL, String directorio, String nombreFichero) throws Exception {
        InputStream is = null;
        FileOutputStream os = null;
        try {
            File f = new File(directorio, nombreFichero);
            if (f.exists()) {
                return false;
            }
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() / 100 != 2) {
                throw new Exception("Código de respuesta inválido: " + connection.getResponseCode() + ", URL: " + strURL);
            }
            is = connection.getInputStream();
            os = new FileOutputStream(f);
            int leido;
            byte[] buf = new byte[MAX_BUFFER_SIZE];
            while ((leido = is.read(buf)) > 0) {
                os.write(buf, 0, leido);
            }
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
        return true;
    }
