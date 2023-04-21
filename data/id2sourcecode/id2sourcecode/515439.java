    private static void collectInfo(URL url) throws IOException {
        System.out.println("\tComposed url>\t" + url);
        URLConnection connection = url.openConnection();
        System.out.println("\tConection>\t" + connection);
        if (connection instanceof HttpsURLConnection) {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) connection;
            httpsURLConnection.setHostnameVerifier(standardVerifier);
            InputStream inputStream = null;
            try {
                inputStream = httpsURLConnection.getInputStream();
                byte[] b = new byte[30];
                int len = inputStream.read(b);
                System.out.println("\tresult>\t\t\t\tOK - initial read returned: " + new String(b, 0, len).replaceAll("\n", "<n>"));
            } catch (Exception e) {
                System.out.println("\tresult>\t\t\t\t(" + e.getClass().getName() + ") - " + e.getMessage());
            }
        }
    }
