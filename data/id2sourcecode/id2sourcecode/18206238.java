    private static boolean getServiceFile(String strServerPath, String strLocalPath) throws Exception {
        URL url = new URL(strServerPath);
        URLConnection ucon = url.openConnection();
        ucon.getDoInput();
        InputStream input = ucon.getInputStream();
        byte[] data = new byte[ucon.getContentLength()];
        input.read(data);
        Performance.printEvent("HTTPRecv service file, size: [" + data.length + "]");
        writeData(strLocalPath, data);
        return true;
    }
