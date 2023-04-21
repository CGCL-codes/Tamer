    private String getServerData(String archivoPhp) {
        InputStream is = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("usuario", Login.usuario.trim()));
        nameValuePairs.add(new BasicNameValuePair("amigoABorrar", amigoABorrar.trim()));
        Log.d("AmigosOffline", "getServerData:" + Login.usuario + ":" + amigoABorrar);
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(archivoPhp);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            Log.e("AmigosOffline", "Error en la conexion http " + e.toString());
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            String line = reader.readLine();
            is.close();
            result = line.trim().toString();
            Log.d("AmigosOffline", "Longitud line: " + line.trim().length());
        } catch (Exception e) {
            Log.e("AmigosOffline", "Error convirtiendo el resultado " + e.toString());
        }
        Log.d("AmigosOffline", "Funciono Json" + result);
        return result;
    }
