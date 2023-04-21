    public static boolean downloadSong(String link, FileOutputStream dest) {
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            BufferedInputStream inp = new BufferedInputStream(connection.getInputStream());
            BufferedOutputStream out = new BufferedOutputStream(dest);
            byte[] array = new byte[SIZE];
            int read;
            while ((read = inp.read(array)) != -1) {
                out.write(array, 0, read);
            }
            inp.close();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
