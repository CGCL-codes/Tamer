    public static String readFileFromURL(URL url) {
        StringBuffer contents = new StringBuffer();
        try {
            InputStream in = url.openStream();
            BufferedReader dis = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = dis.readLine()) != null) {
                contents.append(line + "\n");
            }
            in.close();
        } catch (IOException e) {
            ;
        }
        return contents.toString();
    }
