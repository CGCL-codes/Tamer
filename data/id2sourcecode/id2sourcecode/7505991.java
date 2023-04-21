    public static boolean saveImage(String imageUrl, File destinationFile) {
        try {
            URL url = new URL(imageUrl);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(destinationFile);
            byte[] b = new byte[2048];
            int length;
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
            is.close();
            os.close();
            return true;
        } catch (Exception x) {
            Config.log(WARNING, "Failed to save image: " + imageUrl + " to " + destinationFile, x);
            return false;
        }
    }
