    public static synchronized String getPageContent(String prefix, String host, String parameter) {
        URL url = null;
        URLConnection urlConnection = null;
        HttpURLConnection httpUrlConnection = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        String line = null;
        StringBuilder page = null;
        String finalPage = null;
        try {
            url = new URL(prefix + host + parameter);
            urlConnection = url.openConnection();
            httpUrlConnection = (HttpURLConnection) urlConnection;
            httpUrlConnection.setRequestProperty("User-Agent", "Opera/9.80 (Windows NT 5.1; U; en-GB) Presto/2.2.15 Version/10.00");
            httpUrlConnection.setRequestProperty("Accept", "text/html, application/xml;q=0.9, application/xhtml+xml, image/png, image/jpeg, image/gif, image/x-xbitmap, */*;q=0.1");
            httpUrlConnection.setRequestProperty("Accept-Language", "sk-SK,sk;q=0.9,en;q=0.8");
            httpUrlConnection.setRequestProperty("Accept-Charset", "iso-8859-1, utf-8, utf-16, *;q=0.1");
            httpUrlConnection.setRequestProperty("Referer", prefix + host + parameter);
            httpUrlConnection.setRequestProperty("Connection", "Keep-Alive, TE");
            httpUrlConnection.setRequestProperty("TE", "deflate, gzip, chunked, identity, trailers");
            httpUrlConnection.setReadTimeout(5 * 1000);
            httpUrlConnection.connect();
            inputStreamReader = new InputStreamReader(httpUrlConnection.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            page = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                page.append(line);
                page.append("\n");
            }
        } catch (MalformedURLException e) {
            logger.error("MalformedURLException", e);
        } catch (ProtocolException e) {
            logger.error("ProtocolException", e);
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        } catch (Exception e) {
            logger.error("Exception", e);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
            } catch (IOException e) {
                logger.error("IOException", e);
            } catch (Exception e) {
                logger.error("Exception", e);
            }
        }
        if (page == null) {
            return null;
        } else {
            try {
                finalPage = new String(page.toString().getBytes(), Encoding.UTF_8.getValue());
            } catch (UnsupportedEncodingException e) {
                logger.error("UnsupportedEncodingException", e);
                finalPage = null;
            } catch (Exception e) {
                logger.error("Exception", e);
                finalPage = null;
            }
            return finalPage;
        }
    }
