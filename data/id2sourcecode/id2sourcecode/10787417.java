    public void enregistreSous(File fichier) {
        try {
            ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(fichier));
            ZipEntry entry = new ZipEntry("etude1d.xml");
            zip.putNextEntry(entry);
            ietude1d_.writeTo(zip);
            zip.closeEntry();
            entry = new ZipEntry("reseau.xml");
            zip.putNextEntry(entry);
            djaFrameWriteTo(zip);
            zip.closeEntry();
            zip.close();
            fichier_ = fichier;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
