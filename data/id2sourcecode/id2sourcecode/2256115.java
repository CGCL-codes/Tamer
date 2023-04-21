    public void extractGames(String archiveName) throws IOException {
        byte[] buffer = new byte[4096];
        InputStream is = getClass().getResourceAsStream(archiveName);
        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            if (!entry.isDirectory()) {
                String entryName = entry.getName();
                String gameName = getGameName(entryName);
                if (!gameExists(gameName)) {
                    FileOutputStream fos = new FileOutputStream(toGamePath(gameName));
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int n;
                    while ((n = zis.read(buffer, 0, 4096)) != -1) bos.write(buffer, 0, n);
                    bos.close();
                }
            }
        }
    }
