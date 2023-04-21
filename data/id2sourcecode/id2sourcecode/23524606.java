    private static String[] callURL(String urlToCall) {
        String[] output = new String[2];
        try {
            logger.log(Level.INFO, "servlet url = " + urlToCall);
            URL url = new URL(urlToCall);
            URLConnection urlConnection = url.openConnection();
            InputStream is = null;
            if (((HttpURLConnection) urlConnection).getResponseCode() != 200) {
                is = ((HttpURLConnection) urlConnection).getErrorStream();
            } else {
                is = urlConnection.getInputStream();
            }
            output[0] = stringFromInputStream(is);
            output[1] = urlConnection.getContentType();
            if (is != null) is.close();
        } catch (MalformedURLException me) {
            logger.log(Level.WARNING, "url is malformed (url=" + urlToCall + ")");
            return null;
        } catch (UnsupportedEncodingException uee) {
            logger.log(Level.WARNING, "unsupported encoding used to encode url string");
            return null;
        } catch (IOException e) {
            logger.log(Level.WARNING, "io exception when invoking sesame servlet (url=" + urlToCall + ")");
            return null;
        }
        return output;
    }
