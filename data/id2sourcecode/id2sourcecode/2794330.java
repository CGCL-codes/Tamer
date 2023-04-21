    private String getWebPage(String strURL) {
        URL url = null;
        InputStream is = null;
        DataInputStream dis = null;
        String strLine;
        StringBuilder sbWebPage = new StringBuilder();
        ;
        try {
            url = new URL(strURL);
            is = url.openStream();
            dis = new DataInputStream(new BufferedInputStream(is));
            while ((strLine = dis.readLine()) != null) {
                sbWebPage.append(strLine + "\n");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sbWebPage.toString();
    }
