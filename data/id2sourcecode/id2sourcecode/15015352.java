    private static void recupImage(String idmc, String title) throws ConnectionException {
        try {
            URL url = new URL("http://www.moviecovers.com/getjpg.html/" + encode(toIDMC(idmc)) + ".jpg");
            BufferedInputStream br = new BufferedInputStream(url.openStream());
            File f = new File("image/" + title + ".jpg");
            FileOutputStream out = new FileOutputStream(f);
            byte[] buffer = new byte[100 * 1024];
            int nbLecture;
            while ((nbLecture = br.read(buffer)) != -1) {
                out.write(buffer, 0, nbLecture);
            }
        } catch (Exception e) {
            throw new ConnectionException("Impossible d'�tablir la connection � Moviecovers.com");
        }
    }
