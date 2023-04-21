    public String send() {
        formatCall();
        HttpUriRequest request;
        if (POSTparamList != null && POSTparamList.size() > 0) {
            HttpPost r = new HttpPost(urlFormated);
            try {
                r.setEntity(new UrlEncodedFormEntity(POSTparamList));
            } catch (UnsupportedEncodingException e) {
                Log.e("HTTPPOST", "Error en la codificación de los parámetros.");
                e.printStackTrace();
            }
            request = r;
        } else {
            HttpGet r = new HttpGet(urlFormated);
            request = r;
        }
        try {
            HttpResponse response = new DefaultHttpClient().execute(request);
            return processAnswer(response);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
