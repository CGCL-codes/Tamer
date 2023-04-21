    public static News readNews(String newsXml) {
        News objNews = null;
        try {
            URL datasetsUrl = new URL(newsXml);
            final URLConnection urlConnection = datasetsUrl.openConnection();
            urlConnection.setConnectTimeout(1000);
            urlConnection.setReadTimeout(1000);
            urlConnection.connect();
            final InputStream is = urlConnection.getInputStream();
            objNews = (News) readNewsXml(is);
            is.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (objNews == null) {
            return new News();
        }
        return objNews;
    }
