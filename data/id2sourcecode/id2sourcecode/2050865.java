    public String getUrlAsString(String urlString) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            String zeile;
            Charset charset = Charset.forName(Constants.ENCODING);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), charset));
            while ((zeile = bufferedReader.readLine()) != null) {
                sb.append(zeile);
            }
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return sb.toString();
    }
