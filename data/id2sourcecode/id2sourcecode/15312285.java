    public static void DL(String fAddress, String destinationDir) {
        int slashIndex = fAddress.lastIndexOf('/');
        int periodIndex = fAddress.lastIndexOf('.');
        String fileName = fAddress.substring(slashIndex + 1);
        try {
            URL url = new URL(fAddress);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            InputStream in = null;
            in = url.openStream();
            String content = pipe(in, "utf-8", fileName, destinationDir);
        } catch (Exception e) {
            System.out.println(" Page could not be downloaded, file was deleted go to next URL");
        }
    }
