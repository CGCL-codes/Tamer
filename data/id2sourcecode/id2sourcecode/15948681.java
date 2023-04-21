    private void odszyfrujDane(FileInputStream daneWejsciowe, FileOutputStream daneWyjsciowe, byte[] kluczSesyjny, MarsRamka okno) {
        okno.jLabelTrescKomun1.setText("Proszę czekać. Trwa deszyfrowanie...");
        try {
            byte[] dane = new byte[ROZMIAR_BLOKU];
            Object klucz = MARS_Algorithm.makeKey(kluczSesyjny);
            byte[] kolejnyBlok = new byte[ROZMIAR_BLOKU];
            boolean zapis = false;
            long rozmiarPliku = daneWejsciowe.getChannel().size();
            int liczbaBlokow = (int) rozmiarPliku / ROZMIAR_BLOKU;
            int i = 0;
            if (this.trybSzyfrowania.equalsIgnoreCase("ECB")) {
                while (daneWejsciowe.read(dane) != -1) {
                    if (zapis) daneWyjsciowe.write(kolejnyBlok);
                    kolejnyBlok = MARS_Algorithm.blockDecrypt(dane, 0, klucz);
                    zapis = true;
                    i++;
                    Utils.uaktualnijProgressBar(okno.jProgressBarDeszyfr, i, liczbaBlokow);
                }
            } else if (this.trybSzyfrowania.equalsIgnoreCase("CBC")) {
                byte[] poprzDane = new byte[ROZMIAR_BLOKU];
                while (daneWejsciowe.read(dane) != -1) {
                    if (zapis) daneWyjsciowe.write(kolejnyBlok);
                    kolejnyBlok = MARS_Algorithm.blockDecrypt(dane, 0, klucz);
                    if (!zapis) {
                        kolejnyBlok = Utils.operacjaXor(kolejnyBlok, iv, ROZMIAR_BLOKU);
                    } else {
                        kolejnyBlok = Utils.operacjaXor(kolejnyBlok, poprzDane, ROZMIAR_BLOKU);
                    }
                    System.arraycopy(dane, 0, poprzDane, 0, ROZMIAR_BLOKU);
                    zapis = true;
                    i++;
                    Utils.uaktualnijProgressBar(okno.jProgressBarDeszyfr, i, liczbaBlokow);
                }
            } else if (this.trybSzyfrowania.equalsIgnoreCase("CFB")) {
                byte[] rejestr = new byte[ROZMIAR_BLOKU];
                byte bajtWejsciowy;
                byte bajtWyjsciowy;
                byte[] daneZaszyfrowane = new byte[ROZMIAR_BLOKU];
                rozmiarPliku = rozmiarPliku - 24 - this.dlugoscKluczaZaszyfrowanego;
                rejestr = iv;
                for (i = 0; i < rozmiarPliku - 1; i++) {
                    daneZaszyfrowane = MARS_Algorithm.blockEncrypt(rejestr, 0, klucz);
                    bajtWejsciowy = (byte) daneWejsciowe.read();
                    bajtWyjsciowy = (byte) (daneZaszyfrowane[0] ^ bajtWejsciowy);
                    daneWyjsciowe.write(bajtWyjsciowy);
                    for (int j = 1; j < ROZMIAR_BLOKU; j++) rejestr[j - 1] = rejestr[j];
                    rejestr[ROZMIAR_BLOKU - 1] = bajtWejsciowy;
                    Utils.uaktualnijProgressBar(okno.jProgressBarDeszyfr, i, rozmiarPliku);
                }
                bajtWejsciowy = (byte) daneWejsciowe.read();
                daneZaszyfrowane = MARS_Algorithm.blockEncrypt(rejestr, 0, klucz);
                bajtWyjsciowy = (byte) (daneZaszyfrowane[0] ^ bajtWejsciowy);
                if (bajtWyjsciowy != (byte) 0xAA) throw new Exception();
            } else if (this.trybSzyfrowania.equalsIgnoreCase("OFB")) {
                byte[] rejestr = iv;
                byte bajtWejsciowy;
                byte bajtWyjsciowy;
                byte[] daneZaszyfrowane = new byte[ROZMIAR_BLOKU];
                rozmiarPliku = rozmiarPliku - 24 - this.dlugoscKluczaZaszyfrowanego;
                for (i = 0; i < rozmiarPliku - 1; i++) {
                    daneZaszyfrowane = MARS_Algorithm.blockEncrypt(rejestr, 0, klucz);
                    bajtWejsciowy = (byte) daneWejsciowe.read();
                    bajtWyjsciowy = (byte) (daneZaszyfrowane[0] ^ bajtWejsciowy);
                    daneWyjsciowe.write(bajtWyjsciowy);
                    for (int j = 1; j < ROZMIAR_BLOKU; j++) rejestr[j - 1] = rejestr[j];
                    rejestr[ROZMIAR_BLOKU - 1] = daneZaszyfrowane[0];
                    Utils.uaktualnijProgressBar(okno.jProgressBarDeszyfr, i, rozmiarPliku);
                }
                bajtWejsciowy = (byte) daneWejsciowe.read();
                daneZaszyfrowane = MARS_Algorithm.blockEncrypt(rejestr, 0, klucz);
                bajtWyjsciowy = (byte) (daneZaszyfrowane[0] ^ bajtWejsciowy);
                if (bajtWyjsciowy != (byte) 0xAA) throw new Exception();
            }
            if (zapis) {
                int koniec = kolejnyBlok[kolejnyBlok.length - 1];
                dane = new byte[kolejnyBlok.length - koniec];
                System.arraycopy(kolejnyBlok, 0, dane, 0, dane.length);
                daneWyjsciowe.write(dane);
            }
            okno.jLabelTrescKomun1.setText("Plik został odszyfrowany.");
        } catch (InvalidKeyException ex) {
            Logger.getLogger(SzyfrowaniePliku.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ioe) {
            System.out.println("IOException! " + ioe.getMessage());
            ioe.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(okno, "Błąd deszyfrowania!", "Błąd deszyfrowania", JOptionPane.ERROR_MESSAGE);
            okno.jLabelTrescKomun1.setText("Plik nie został odszyfrowany.");
            File plik = new File(this.plikWynikowy);
            plik.delete();
        }
    }
