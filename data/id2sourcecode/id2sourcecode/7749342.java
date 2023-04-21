    public static synchronized String ReadURLString(URL url) throws IOException {
        try {
            StatisticsStorage.numberofdownloadedbytes += EstimateLengthOfPage(url);
            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            String line = "";
            int i = 0;
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
