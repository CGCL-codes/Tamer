    private URL parserRealDownloadUrl() {
        String link = downloadInfo.getLink();
        link = link.replaceAll(" ", "%20");
        URL parserRealDownloadUrl = null;
        String urlStr = null;
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                byte[] buffer = new byte[2048];
                String content = null;
                int len = 0;
                Pattern purl = Pattern.compile("href=\"(.*)\"");
                while ((len = inputStream.read(buffer)) != -1) {
                    content = new String(buffer, 0, len);
                    Matcher murl = purl.matcher(content);
                    if (murl.find()) {
                        urlStr = murl.group(1);
                        break;
                    }
                }
                inputStream.close();
            }
            parserRealDownloadUrl = new URL(urlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parserRealDownloadUrl;
    }
