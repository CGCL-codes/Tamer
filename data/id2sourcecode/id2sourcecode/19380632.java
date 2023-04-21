    private String getServerData(String returnString) {
        InputStream is = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("usuario", Login.usuario));
        nameValuePairs.add(new BasicNameValuePair("amigoACopiar", amigoACopiar.trim()));
        nameValuePairs.add(new BasicNameValuePair("grupoSeleccionado", grupoSeleccionado.trim()));
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(returnString);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            Log.e("AmigosOnline", "Error en la conexion http " + e.toString());
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            String line = reader.readLine();
            is.close();
            result = line.trim().toString();
            Log.d("AmigosTodos", "Longitud line: " + line.trim().length());
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }
        Log.d("AmigosTodos", "Funciono json" + result);
        return result;
    }
