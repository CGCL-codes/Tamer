    @Override
    protected String doInBackground(Void... params) {
        HttpPost prequest = null;
        HttpGet grequest = null;
        if (POSTparamList != null && POSTparamList.size() > 0) {
            prequest = new HttpPost(urlFormated);
            try {
                prequest.setEntity(new UrlEncodedFormEntity(POSTparamList));
            } catch (UnsupportedEncodingException e) {
                Log.e("HTTPPOST", "Error en la codificación de los parámetros.");
                e.printStackTrace();
            }
        } else {
            grequest = new HttpGet(urlFormated);
        }
        try {
            HttpResponse response = new DefaultHttpClient().execute((prequest != null) ? prequest : grequest);
            return processAnswer(response);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
