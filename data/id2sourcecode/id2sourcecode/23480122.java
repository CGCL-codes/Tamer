    private void initLecture(File pFile) throws FileNotFoundException, IOException {
        final FileChannel lFileChannel = new FileInputStream(pFile).getChannel();
        final int lTailleFileChannel = (int) lFileChannel.size();
        final MappedByteBuffer lMappedByteBuffer = lFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, lTailleFileChannel);
        final Charset lCharset = Charset.forName(java.util.ResourceBundle.getBundle("txt/txt").getString("MoteurLettre.xml.encodage"));
        final CharsetDecoder lCharsetDecoder = lCharset.newDecoder();
        final CharBuffer lCharBuffer = lCharsetDecoder.decode(lMappedByteBuffer);
        aListeContenu = new ArrayList<String>();
        final char[] lCharTab = new char[2048];
        String lBufStr;
        int i;
        int lIndiceDebutBalise = -1;
        char lCarLu;
        for (i = 0; lCharBuffer.hasRemaining(); i++) {
            lCarLu = lCharBuffer.get();
            if (i >= lCharTab.length) {
                lBufStr = String.valueOf(lCharTab, 0, ((lIndiceDebutBalise == -1) ? i : lIndiceDebutBalise));
                aListeContenu.add(lBufStr);
                if (lIndiceDebutBalise == -1) {
                    i = 0;
                } else {
                    for (int j = lIndiceDebutBalise, k = 0; j < lCharTab.length; j++, k++) {
                        lCharTab[k] = lCharTab[j];
                    }
                    i = i - lIndiceDebutBalise;
                    lIndiceDebutBalise = 0;
                }
            }
            lCharTab[i] = lCarLu;
            if (i > 2 && lCharTab[i - 3] == '&' && lCharTab[i - 2] == 'g' && lCharTab[i - 1] == 't' && lCharTab[i] == ';') {
                final String lBaliseInconnue = new String(lCharTab, lIndiceDebutBalise, i + 1 - lIndiceDebutBalise);
                if (aDonneesProgramme.getEnsembleDonnees().getEnsembleDonnees().get(0).getDonneesMap().get(lBaliseInconnue) != null) {
                    final String lContenuAvantBalise = new String(lCharTab, 0, lIndiceDebutBalise);
                    aListeContenu.add(lContenuAvantBalise);
                    aListeContenu.add(lBaliseInconnue);
                    lIndiceDebutBalise = -1;
                    i = -1;
                }
            } else if (i > 2 && lCharTab[i - 3] == '&' && lCharTab[i - 2] == 'l' && lCharTab[i - 1] == 't' && lCharTab[i] == ';') {
                lIndiceDebutBalise = i - 3;
            }
        }
        lBufStr = String.valueOf(lCharTab, 0, i);
        aListeContenu.add(lBufStr);
        lFileChannel.close();
    }
