    public static void encode(TheMatrix m, String outFileName) {
        try {
            System.out.println("Compressing " + outFileName + " ...");
            ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(outFileName + EXT));
            zipout.setMethod(ZipOutputStream.DEFLATED);
            zipout.setLevel(9);
            ZipEntry entry = new ZipEntry(outFileName + ".txt");
            zipout.putNextEntry(entry);
            PrintWriter out = new PrintWriter(zipout);
            System.out.println("Writing data ...");
            out.println(m.getImageHeight() + " " + m.getImageWidth());
            for (int x = 0; x < m.getImageWidth(); ++x) {
                for (int y = 0; y < m.getImageHeight(); ++y) {
                    Color c = m.getColor(x, y);
                    out.println(c.getRGB());
                }
            }
            out.close();
            zipout.close();
            System.out.println("encoding done");
        } catch (IOException e) {
            throw new Error("Error: " + e);
        }
    }
