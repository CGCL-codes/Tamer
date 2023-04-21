    private void zaszyfrujDane(FileInputStream daneWejsciowe, FileOutputStream daneWyjsciowe, byte[] kluczSesyjny, MarsRamka okno) {
        okno.jLabelTrescKomun.setText("Proszę czekać. Trwa szyfrowanie...");
        try {
            byte[] dane = new byte[ROZMIAR_BLOKU];
            Object klucz = MARS_Algorithm.makeKey(kluczSesyjny);
            long rozmiarPliku = daneWejsciowe.getChannel().size();
            int liczbaBlokow = (int) rozmiarPliku / ROZMIAR_BLOKU;
            int reszta = (int) rozmiarPliku % ROZMIAR_BLOKU;
            if (this.trybSzyfrowania.equalsIgnoreCase("ECB")) {
                for (int i = 0; i < liczbaBlokow; i++) {
                    daneWejsciowe.read(dane);
                    daneWyjsciowe.write(MARS_Algorithm.blockEncrypt(dane, 0, klucz));
                    Utils.uaktualnijProgressBar(okno.jProgressBarSzyfr, i, liczbaBlokow);
                }
                dane = dopelnij(daneWejsciowe, reszta);
                daneWyjsciowe.write(MARS_Algorithm.blockEncrypt(dane, 0, klucz));
            } else if (this.trybSzyfrowania.equalsIgnoreCase("CBC")) {
                byte[] danePoOperacjiXor = new byte[ROZMIAR_BLOKU];
                byte[] daneZaszyfrowane = new byte[ROZMIAR_BLOKU];
                for (int i = 0; i < liczbaBlokow; i++) {
                    daneWejsciowe.read(dane);
                    if (i == 0) {
                        danePoOperacjiXor = Utils.operacjaXor(dane, iv, ROZMIAR_BLOKU);
                    } else {
                        danePoOperacjiXor = Utils.operacjaXor(dane, daneZaszyfrowane, ROZMIAR_BLOKU);
                    }
                    daneZaszyfrowane = MARS_Algorithm.blockEncrypt(danePoOperacjiXor, 0, klucz);
                    daneWyjsciowe.write(daneZaszyfrowane);
                    Utils.uaktualnijProgressBar(okno.jProgressBarSzyfr, i, liczbaBlokow);
                }
                dane = dopelnij(daneWejsciowe, reszta);
                if (liczbaBlokow == 0) {
                    danePoOperacjiXor = Utils.operacjaXor(dane, iv, ROZMIAR_BLOKU);
                } else {
                    danePoOperacjiXor = Utils.operacjaXor(dane, daneZaszyfrowane, ROZMIAR_BLOKU);
                }
                daneZaszyfrowane = MARS_Algorithm.blockEncrypt(danePoOperacjiXor, 0, klucz);
                daneWyjsciowe.write(daneZaszyfrowane);
            } else if (this.trybSzyfrowania.equalsIgnoreCase("CFB")) {
                byte[] rejestr = new byte[ROZMIAR_BLOKU];
                byte bajtWejsciowy;
                byte bajtWyjsciowy;
                byte[] daneZaszyfrowane = new byte[ROZMIAR_BLOKU];
                rejestr = iv;
                for (int i = 0; i < rozmiarPliku; i++) {
                    daneZaszyfrowane = MARS_Algorithm.blockEncrypt(rejestr, 0, klucz);
                    bajtWejsciowy = (byte) daneWejsciowe.read();
                    bajtWyjsciowy = (byte) (daneZaszyfrowane[0] ^ bajtWejsciowy);
                    daneWyjsciowe.write(bajtWyjsciowy);
                    for (int j = 1; j < ROZMIAR_BLOKU; j++) rejestr[j - 1] = rejestr[j];
                    rejestr[ROZMIAR_BLOKU - 1] = bajtWyjsciowy;
                    Utils.uaktualnijProgressBar(okno.jProgressBarSzyfr, i, rozmiarPliku);
                }
                byte ostatni = (byte) 0xAA;
                daneZaszyfrowane = MARS_Algorithm.blockEncrypt(rejestr, 0, klucz);
                bajtWyjsciowy = (byte) (daneZaszyfrowane[0] ^ ostatni);
                daneWyjsciowe.write(bajtWyjsciowy);
            } else if (this.trybSzyfrowania.equalsIgnoreCase("OFB")) {
                byte[] rejestr = iv;
                byte bajtWejsciowy;
                byte bajtWyjsciowy;
                byte[] daneZaszyfrowane = new byte[ROZMIAR_BLOKU];
                for (int i = 0; i < rozmiarPliku; i++) {
                    daneZaszyfrowane = MARS_Algorithm.blockEncrypt(rejestr, 0, klucz);
                    bajtWejsciowy = (byte) daneWejsciowe.read();
                    bajtWyjsciowy = (byte) (daneZaszyfrowane[0] ^ bajtWejsciowy);
                    daneWyjsciowe.write(bajtWyjsciowy);
                    for (int j = 1; j < ROZMIAR_BLOKU; j++) rejestr[j - 1] = rejestr[j];
                    rejestr[ROZMIAR_BLOKU - 1] = daneZaszyfrowane[0];
                    Utils.uaktualnijProgressBar(okno.jProgressBarSzyfr, i, rozmiarPliku);
                }
                byte ostatni = (byte) 0xAA;
                daneZaszyfrowane = MARS_Algorithm.blockEncrypt(rejestr, 0, klucz);
                bajtWyjsciowy = (byte) (daneZaszyfrowane[0] ^ ostatni);
                daneWyjsciowe.write(bajtWyjsciowy);
            }
            okno.jLabelTrescKomun.setText("Plik został zaszyfrowany.");
        } catch (InvalidKeyException ex) {
            Logger.getLogger(SzyfrowaniePliku.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ioe) {
            System.out.println("IOException! " + ioe.getMessage());
            ioe.printStackTrace();
        } catch (Exception e) {
            okno.jLabelTrescKomun.setText("Plik nie został zaszyfrowany.");
        }
    }
