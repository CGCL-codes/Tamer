    public static String getText(URL url) {
        String text = null;
        try {
            InputStream stream = url.openStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(stream));
            String inputLine;
            text = "";
            while ((inputLine = in.readLine()) != null) {
                text += inputLine + "\n";
            }
            in.close();
        } catch (MalformedURLException ex) {
            logger.warning(ex.getMessage());
            return null;
        } catch (IOException ex) {
            logger.warning(ex.getMessage());
            return null;
        }
        return text;
    }
