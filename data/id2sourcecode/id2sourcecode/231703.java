    private static String doTranslateText(String text) throws IOException {
        String strUrl = "http://www.tranexp.com:2000/InterTran?" + "url=http%3A%2F%2F" + "&type=text" + "&from=eng" + "&to=wel" + "&text=" + URLEncoder.encode(text, "UTF-8");
        System.out.println("URL: " + strUrl);
        System.getProperties().put("proxySet", "true");
        System.getProperties().put("proxyHost", " cache-haw2.cableinet.co.uk");
        System.getProperties().put("proxyPort", "8080");
        HttpURLConnection url = (HttpURLConnection) (new URL(strUrl)).openConnection();
        url.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; America Online Browser 1.1; rev1.1; Windows NT 5.1;)");
        url.connect();
        DataInputStream in = new DataInputStream(url.getInputStream());
        StringBuffer sb = new StringBuffer();
        if (url.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try {
                while (true) {
                    sb.append((char) in.readUnsignedByte());
                }
            } catch (EOFException e) {
            } catch (IOException e) {
                throw new RuntimeException(e + ": " + e.getMessage());
            }
        } else {
            throw new RuntimeException(url.getResponseMessage());
        }
        String doc = sb.toString();
        Pattern pattern = Pattern.compile("<textarea.*?name=\"translation\".*?>(.*?)</textarea>");
        Matcher matcher = pattern.matcher(doc);
        String result = "";
        if (matcher.find()) {
            result = matcher.group(1);
        } else {
            throw new RuntimeException("No translation received");
        }
        return result;
    }
