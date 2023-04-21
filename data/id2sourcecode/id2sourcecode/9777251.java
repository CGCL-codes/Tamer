    public void nuovoSegretario(Segretario segretario) throws ApplicationException {
        try {
            DataManager dataManager = getDataManager();
            String nomeUtente = String.valueOf(segretario.getNome().charAt(0)).toLowerCase() + ".";
            nomeUtente += segretario.getCognome().toLowerCase();
            segretario.setNomeUtente(nomeUtente);
            String password = AuthManager.creaPassword(Integer.parseInt(getSettings().getProperty("passwordLength", "8")));
            String passwordHash = AuthManager.digest(password);
            segretario.setPassword(passwordHash);
            if (dataManager.getUtenteFromNomeUtente(segretario.getNomeUtente()) != null) throw new StatoDocenteException(StatoDocenteException.PRESENTE);
            if (dataManager.getRegistrazioneFromUtente(segretario.getNomeUtente()) != null) throw new StatoDocenteException(StatoDocenteException.REGISTRATO);
            dataManager.insertEntity(segretario);
            Registrazione registrazione = new Registrazione(0, segretario.getNomeUtente(), new Timestamp(System.currentTimeMillis()), "segretario", true);
            dataManager.insertEntity(registrazione);
            Logger.log(dataManager, Operazione.NUOVA_REGISTRAZIONE, getRegistrazione(), segretario.getNomeUtente() + " aggiunto e registrato al servizio");
            String testoEmail = getResManager().getResource("newSegretarioMail");
            testoEmail = testoEmail.replaceAll("<password>", password);
            testoEmail = testoEmail.replaceAll("<nomeUtente>", segretario.getNomeUtente());
            String soggetto = getSettings().getProperty("newSegretarioMailSubject");
            if (!Mailer.sendMail(testoEmail, soggetto, segretario.getEmail())) throw new ApplicationException("Errore nell'invio dell'email");
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        } catch (NoSuchAlgorithmException e) {
            throw new ApplicationException("Errore");
        }
    }
