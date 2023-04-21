    public URLConnection abreConeccion() throws IOException {
        ver("Intentando crear el socket");
        urlConnection = url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setAllowUserInteraction(true);
        if (urlConnection != null) {
            setConexion(true);
            archivoInputStream = requestHTTP(url, urlConnection);
            longitudArchivo = leerLongitudArchivo();
        }
        return urlConnection;
    }
