    public void startMIDlet() {
        switchDisplayable(null, getFrmImmissioneDati());
        switchDisplayable(null, getFrmInfo());
        frmInfo.setString("Caricamento dati login...");
        getLogin();
        frmInfo.setString("Caricamento canali...");
        getChannels();
        frmInfo.setString("Caricamento liste...");
        getOtherLists();
        frmInfo.setString("Impostazione canali...");
        impostaCanali(DefaultRecType);
        switchDisplayable(null, getFrmImmissioneDati());
    }
