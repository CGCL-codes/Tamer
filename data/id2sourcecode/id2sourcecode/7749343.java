    public static String ReadGZIPURLString(URL url) throws IOException {
        try {
            InputStream is = url.openStream();
            GZIPInputStream zipin = new GZIPInputStream(is);
            InputStreamReader isr = new InputStreamReader(zipin);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            String line = "";
            while ((inputLine = in.readLine()) != null) {
                line += inputLine + "\n";
            }
            is.close();
            isr.close();
            in.close();
            return line;
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
