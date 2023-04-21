    public static Operazione log(DataManager dm, String tipo, Registrazione utente, String descrizione) throws DataAccessException, NoSuchAlgorithmException {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Operazione operazione = new Operazione();
        operazione.setUtente(utente.getIdRegistrazione());
        operazione.setTipo(tipo);
        operazione.setDataEsecuzione(now);
        operazione.setDescrizione(descrizione);
        dm.insertEntity(operazione);
        String hash = operazione.getIdOperazione() + tipo + descrizione + utente.getUtente() + now.toString();
        hash = AuthManager.digest(hash);
        operazione.setHash(hash);
        dm.updateEntity(operazione);
        return operazione;
    }
