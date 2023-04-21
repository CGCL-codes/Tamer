    public static void downloadFile(String urlstr, String destino) throws MalformedURLException, IOException {
        long startTime = System.currentTimeMillis();
        System.out.println("Connecting to Mura site...\n");
        URL url = new URL(urlstr);
        url.openConnection();
        InputStream reader = url.openStream();
        FileOutputStream writer = new FileOutputStream(destino);
        byte[] buffer = new byte[153600];
        int totalBytesRead = 0;
        int bytesRead = 0;
        System.out.println("Reading ZIP file 150KB blocks at a time.\n");
        while ((bytesRead = reader.read(buffer)) > 0) {
            writer.write(buffer, 0, bytesRead);
            buffer = new byte[153600];
            totalBytesRead += bytesRead;
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Done. " + (new Integer(totalBytesRead).toString()) + " bytes read (" + (new Long(endTime - startTime).toString()) + " millseconds).\n");
        writer.close();
        reader.close();
    }
