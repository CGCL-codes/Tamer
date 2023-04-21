    public static AdoreProcess getServiceRegistryResponse(String baseUrl, String id) {
        AdoreProcess response = new AdoreProcess();
        try {
            URL url = new URL(baseUrl.toString() + "?url_ver=Z39.88-2004" + "&rft_id=" + URLEncoder.encode(id, "UTF-8") + "&svc_val_fmt=info%3Aofi%2Ffmt%3Akev%3Amtx%3Adc&svc_id=info%3Alanl-repo%2Fsvc%2Fockham");
            response.setRequest(url.toString());
            HttpURLConnection huc = (HttpURLConnection) (url.openConnection());
            int code = huc.getResponseCode();
            if (code == 200) {
                InputStream is = huc.getInputStream();
                response.setResponse(new String(StreamUtil.getByteArray(is)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
