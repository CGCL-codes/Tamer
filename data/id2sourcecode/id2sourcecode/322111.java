    private static String loadFromUrl(String pageUrl) throws IOException {
        try {
            URL url = new URL(pageUrl);
            URLConnection urlConnection = url.openConnection();
            String currentUserAgent1 = currentUserAgent;
            currentUserAgent1 = currentUserAgent1.replaceAll("%X%", String.valueOf(((int) (rnd.nextDouble() * 30000))));
            urlConnection.setRequestProperty("User-Agent", currentUserAgent1);
            urlConnection.setRequestProperty("Referer", "http://bingobanko.tv2.dk/print/");
            InputStream urlIs = urlConnection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(urlIs));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            urlIs.close();
            String s = sb.toString();
            return s;
        } catch (IOException e) {
            return null;
        }
    }
