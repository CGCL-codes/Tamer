    public static void download(File file, String image, JProgressBar bar) {
        bar.setValue(0);
        URL url = null;
        BufferedOutputStream fOut = null;
        try {
            file.createNewFile();
            url = new URL(image);
            InputStream html = null;
            html = url.openStream();
            fOut = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buffer = new byte[32 * 1024];
            int bytesRead = 0;
            while ((bytesRead = html.read(buffer)) != -1) {
                bar.setValue(bar.getValue() + bytesRead);
                fOut.write(buffer, 0, bytesRead);
            }
            html.close();
            fOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
