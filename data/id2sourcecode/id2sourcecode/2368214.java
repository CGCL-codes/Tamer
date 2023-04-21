    public String download(String uri, int timeout) {
        Log.i(TAG, "Descargando: " + uri);
        String resultado = "";
        cancelled = false;
        try {
            URL url = new URL(uri);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setConnectTimeout(timeout);
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = httpConnection.getInputStream();
                resultado = convertStreamToString(in);
                Log.i(TAG, "Resultado: " + resultado);
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error download: " + ex.getMessage().toString());
        }
        if (cancelled) return "";
        return resultado;
    }
