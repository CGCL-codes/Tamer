    public HttpURLConnection reAbreConeccion() throws IOException {
        ver("reAbreConeccion(): Intentando crear el socket");
        httpURLConnection = (HttpURLConnection) url.openConnection();
        if (httpURLConnection != null) {
            setConexion(true);
            reconnected = true;
            ver("reAbreConeccion(): " + " Offset = " + offset + " LongitudArchivo = " + longitudArchivo);
            archivoInputStream = requestRangedHTTP(url, httpURLConnection, offset + "", longitudArchivo + "");
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
