    public String getFile(URL urlFile) throws IOException {
        URLConnection urlConn = urlFile.openConnection();
        @SuppressWarnings("unused") String FileType = urlConn.getContentType();
        int FileLenght = urlConn.getContentLength();
        if (FileLenght == -1) {
            throw new IOException("Fichier non valide.");
        }
        InputStream brut = urlConn.getInputStream();
        InputStream entree = new BufferedInputStream(brut);
        byte[] donnees = new byte[FileLenght];
        int BitRead = 0;
        int deplacement = 0;
        lLoading.setText("Download " + urlConn.toString().substring(urlConn.toString().lastIndexOf('/') + 1));
        pbLoading.setMinimum(0);
        pbLoading.setMaximum(FileLenght);
        pbLoading.setValue(deplacement);
        while (deplacement < FileLenght) {
            BitRead = entree.read(donnees, deplacement, donnees.length - deplacement);
            if (BitRead == -1) break;
            deplacement += BitRead;
            pbLoading.setValue(deplacement);
        }
        entree.close();
        if (deplacement != FileLenght) {
            throw new IOException("Nous n'avons lu que " + deplacement + " octets au lieu des " + FileLenght + " attendus");
        }
        String FileName = urlFile.getFile();
        FileName = FileName.substring(FileName.lastIndexOf('/') + 1);
        FileOutputStream WritenFile = new FileOutputStream(FileName);
        WritenFile.write(donnees);
        WritenFile.flush();
        WritenFile.close();
        return FileName;
    }
