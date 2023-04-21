    public static String fetch(String address) throws MalformedURLException, IOException {
        URL url = new URL(address);
        URLConnection connection = url.openConnection();
        DataInputStream in = new DataInputStream(connection.getInputStream());
        String line = in.readLine();
        String all = "";
        while (line != null) {
            all += line;
            line = in.readLine();
        }
        return all;
    }
