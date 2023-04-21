    @Override
    public String downloadImageFromUrl(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setAllowUserInteraction(false);
        conn.setRequestProperty("Content-Type", "image");
        String[] urlArr = urlString.split("[/]");
        String filename = urlArr[urlArr.length - 1];
        String uploadedFileName = uploadProc.storeImage(conn.getInputStream(), filename, true);
        return uploadedFileName;
    }
