    public static Font[] loadFont(File file, byte[]... expectedFingerprints) throws IOException {
        Font[] fonts = loadFont(file, true);
        for (int i = 0; i < Math.min(fonts.length, expectedFingerprints.length); i++) {
            if (expectedFingerprints[i] != fonts[i].digest()) {
                throw new IOException("Did not get the expected fingerprint for font#" + i + " in " + file.getPath() + " Has the file changed?");
            }
        }
        return fonts;
    }
