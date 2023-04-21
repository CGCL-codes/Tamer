    public HttpURLConnection abreConeccion() throws IOException {
        httpURLConnection = (HttpURLConnection) url.openConnection();
        log.info("--> TRACE: abreConeccion(): Conexion URL creada");
        if (httpURLConnection != null) {
            setConexion(true);
            archivoInputStream = requestHTTP(url, httpURLConnection);
            longitudArchivo = leerLongitudArchivo();
            for (int i = 0; ; i++) {
                String name = httpURLConnection.getHeaderFieldKey(i);
                String value = httpURLConnection.getHeaderField(i);
                if (name == null && value == null) {
                    break;
                }
                if (name == null) {
                    System.out.println("\nServer HTTP version, Response code:");
                    System.out.println(value);
                    System.out.print("\n");
                } else {
                    System.out.println(name + "=" + value + "\n");
                }
            }
        }
        return httpURLConnection;
    }
