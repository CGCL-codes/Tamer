    public static File downloadTorrentFile(String url, String save) {
        try {
            HttpClient client = new HttpClient();
            HttpMethod method = new GetMethod(url);
            method.setFollowRedirects(true);
            client.executeMethod(method);
            InputStream is = method.getResponseBodyAsStream();
            File file = new File(save);
            FileOutputStream out = new FileOutputStream(file);
            int b;
            while ((b = is.read()) != -1) out.write(b);
            return file;
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
