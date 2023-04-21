    private void updateDNS(String hostName, String updateString) throws IOException {
        System.out.println("Update ip for host " + hostName + ".....");
        System.out.println(updateString);
        URL url = new URL(updateString);
        URLConnection urlConnection = url.openConnection();
        InputStream inStream = urlConnection.getInputStream();
        System.out.println("***************************************");
        System.out.println(IOUtils.toString(inStream));
        System.out.println("***************************************");
    }
