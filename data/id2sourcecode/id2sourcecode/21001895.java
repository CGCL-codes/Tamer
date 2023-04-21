    public static void saveUrlToFile(File saveFile, String location) {
        URL url;
        try {
            url = new URL(location);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            BufferedWriter out = new BufferedWriter(new FileWriter(saveFile));
            char[] cbuf = new char[255];
            while ((in.read(cbuf)) != -1) {
                out.write(cbuf);
            }
            in.close();
            out.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
