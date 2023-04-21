    private String getServerData(String returnString) {
        InputStream is = null;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("solicitado", Login.usuario));
        Log.d("Pendientes", "Select: " + selec);
        nameValuePairs.add(new BasicNameValuePair("solicita", selec));
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(returnString);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.d("Pendientes", "Funciono enviadas http connection ");
        } catch (Exception e) {
            Log.e("Pendientes", "Error en conexion http " + e.toString());
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            String line = reader.readLine();
            is.close();
            result = line.trim().toString();
            Log.d("Pendientes", "Longitud line: " + line.trim().length());
        } catch (Exception e) {
            Log.e("Pendientes", "Error convirtiendo resultado: " + e.toString());
        }
        Log.d("Pendientes", "Funciono Json: " + result);
        return result;
    }
