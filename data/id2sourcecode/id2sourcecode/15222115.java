    private String getServerData(String returnString) {
        InputStream is = null;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("solicita", Login.usuario));
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(auth);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.d("Enviadas", "Funciono enviadas, http connection ");
        } catch (Exception e) {
            Log.e("Enviadas", "Error en conexion http " + e.toString());
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            String line = reader.readLine();
            is.close();
            result = line.trim().toString();
            Log.d("Enviadas", "Longitud line: " + line.trim().length());
        } catch (Exception e) {
            Log.e("Enviadas", "Error convirtiendo el resultado: " + e.toString());
        }
        Log.d("Enviadas", "Funciono Json: " + result);
        return result;
    }
