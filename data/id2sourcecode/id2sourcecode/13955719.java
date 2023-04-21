    public static void zipRepertoire(final ZipOutputStream pZipOutputStream, final File pFichiersODT, final String pNomRepertoire, final boolean pIsRacine) throws FileNotFoundException, IOException {
        final String lNomFichierDansArchive;
        if (pIsRacine) {
            lNomFichierDansArchive = "";
        } else {
            lNomFichierDansArchive = new StringBuilder(pNomRepertoire).append(pFichiersODT.getName()).append(pFichiersODT.isDirectory() ? '/' : "").toString();
        }
        if (pFichiersODT.isDirectory()) {
            for (final File lFichier : pFichiersODT.listFiles()) zipRepertoire(pZipOutputStream, lFichier, lNomFichierDansArchive, false);
            return;
        }
        BufferedInputStream lBufferedInputStream = null;
        final int lTailleBuffer = 1024;
        final byte[] lBuffer = new byte[lTailleBuffer];
        try {
            lBufferedInputStream = new BufferedInputStream(new FileInputStream(pFichiersODT), lTailleBuffer);
            final ZipEntry lZipEntry = new ZipEntry(lNomFichierDansArchive);
            pZipOutputStream.putNextEntry(lZipEntry);
            int lNbCaracsLus;
            while ((lNbCaracsLus = lBufferedInputStream.read(lBuffer, 0, lTailleBuffer)) != -1) {
                pZipOutputStream.write(lBuffer, 0, lNbCaracsLus);
            }
            pZipOutputStream.closeEntry();
        } finally {
            if (lBufferedInputStream != null) {
                lBufferedInputStream.close();
            }
        }
    }
