    public static AdoreProcess getIdLocatorResponse(String baseUrl, String id) throws IdNotFoundException {
        AdoreProcess response = new AdoreProcess();
        try {
            URL url = new URL(baseUrl.toString() + "?url_ver=Z39.88-2004" + "&rft_id=" + URLEncoder.encode(id, "UTF-8"));
            response.setRequest(url.toString());
            HttpURLConnection huc = (HttpURLConnection) (url.openConnection());
            int code = huc.getResponseCode();
            if (code == 200) {
                InputStream is = huc.getInputStream();
                response.setResponse(new String(StreamUtil.getByteArray(is)));
            } else if (code == 404) {
                throw new IdNotFoundException("Unable to locate specified id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
