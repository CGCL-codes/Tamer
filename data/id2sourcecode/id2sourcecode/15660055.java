    public static void downloadPlugin(String urlString, String fileName) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(urlString);
        httpGet.addHeader("User-Agent", "Genedator Plugin Manager");
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(entity.getContent());
            bos = new BufferedOutputStream(new FileOutputStream("plugins/" + fileName));
            int i;
            while ((i = bis.read()) != -1) {
                bos.write(i);
            }
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
    }
