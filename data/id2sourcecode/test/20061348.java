    public static boolean isSFIFile(String sUrl) {
        boolean bSFI = false;
        sUrl = createUrlString(sUrl);
        try {
            URL url = new URL(sUrl);
            URLConnection connection = url.openConnection();
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String sLn = "";
            while ((sLn = br.readLine()) != null) {
                if (sLn.indexOf("<a name=\"h0\"></a>") != -1) {
                    bSFI = true;
                    break;
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("HtmlUtilities.isSFIFile: " + sUrl + e.toString());
        }
        return bSFI;
    }
