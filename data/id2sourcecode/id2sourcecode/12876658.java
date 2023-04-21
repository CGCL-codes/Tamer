    public void getChannels() {
        System.out.println("Leggo liste canali/stazioni..");
        try {
            InputStream is = null;
            int MAXLEN = 10000;
            byte[] data = new byte[MAXLEN];
            String contenuto;
            String chan;
            System.out.println("########TV#########");
            is = getClass().getResourceAsStream("tv.txt");
            if (is != null) {
                contenuto = new String(data, 0, is.read(data, 0, MAXLEN));
                NumTV = 0;
                while (contenuto.length() > 0) {
                    int finechan = contenuto.indexOf('\n') - 1;
                    chan = contenuto.substring(0, finechan);
                    TVChannelName[NumTV] = chan;
                    NumTV++;
                    contenuto = contenuto.substring(finechan + 2, contenuto.length());
                }
                is.close();
                System.out.println("Canali TV: " + Integer.toString(NumTV));
            } else {
                System.out.println("Errore nell'accesso alla lista dei canali TV.");
                txtRisultato.setString(txtRisultato.getString() + "\nErrore nell'accesso alla lista dei canali TV.");
            }
            System.out.println("########Radio#########");
            is = getClass().getResourceAsStream("radio.txt");
            if (is != null) {
                NumRadio = 0;
                contenuto = new String(data, 0, is.read(data, 0, MAXLEN));
                while (contenuto.length() > 0) {
                    int finechan = contenuto.indexOf('\n') - 1;
                    chan = contenuto.substring(0, finechan);
                    RadioChannelName[NumRadio] = chan;
                    NumRadio++;
                    contenuto = contenuto.substring(finechan + 2, contenuto.length());
                }
                is.close();
                System.out.println("Canali radio: " + Integer.toString(NumRadio));
            } else {
                System.out.println("Errore nell'accesso alla lista dei canali Radio.");
                txtRisultato.setString(txtRisultato.getString() + "\nErrore nell'accesso alla lista dei canali Radio.");
            }
        } catch (java.io.IOException ex) {
            System.out.println("Eccezione: errore generico nella lettura dell'elenco canali Radio/TV.");
            txtRisultato.setString(txtRisultato.getString() + "\nEccezione: errore generico nella lettura dell'elenco canali Radio/TV.");
            ex.printStackTrace();
        }
        System.out.println("Fatto.");
    }
