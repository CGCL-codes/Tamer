    File downloadKgml(final File targetDirectory, final String pathway) throws SocketException, IOException {
        if (targetDirectory.exists()) {
            if (!targetDirectory.delete()) {
                throw new IOException();
            }
        }
        if (!targetDirectory.mkdir()) {
            throw new IOException();
        }
        final URL pathwayUrl = new URL("http://www.genome.jp/kegg-bin/download?entry=" + pathway + "&format=kgml");
        final File kgmlFile = new File(targetDirectory, pathway + ".xml");
        fileUtils.write(kgmlFile, fileUtils.read(pathwayUrl));
        return kgmlFile;
    }
