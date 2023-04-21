    public void nuovoDocente(Docente docente) throws ApplicationException {
        try {
            DataManager dataManager = getDataManager();
            String nomeUtente = String.valueOf(docente.getNome().charAt(0)).toLowerCase() + ".";
            nomeUtente += docente.getCognome().toLowerCase();
            docente.setNomeUtente(nomeUtente);
            String password = AuthManager.creaPassword(Integer.parseInt(getSettings().getProperty("passwordLength", "8")));
            String passwordHash = AuthManager.digest(password);
            docente.setPassword(passwordHash);
            if (dataManager.getUtenteFromNomeUtente(docente.getNomeUtente()) != null) throw new StatoDocenteException(StatoDocenteException.PRESENTE);
            if (dataManager.getRegistrazioneFromUtente(docente.getNomeUtente()) != null) throw new StatoDocenteException(StatoDocenteException.REGISTRATO);
            dataManager.insertEntity(docente);
            Registrazione registrazione = new Registrazione(0, docente.getNomeUtente(), new Timestamp(System.currentTimeMillis()), "docente", true);
            dataManager.insertEntity(registrazione);
            Logger.log(dataManager, Operazione.NUOVA_REGISTRAZIONE, getRegistrazione(), docente.getNomeUtente() + " aggiunto e registrato al servizio");
            String testoEmail = getResManager().getResource("newDocenteMail");
            testoEmail = testoEmail.replaceAll("<password>", password);
            testoEmail = testoEmail.replaceAll("<nomeUtente>", docente.getNomeUtente());
            String soggetto = getSettings().getProperty("newDocenteMailSubject");
            if (!Mailer.sendMail(testoEmail, soggetto, docente.getEmail())) throw new ApplicationException("Errore nell'invio dell'email");
        } catch (DataAccessException e) {
            throw new ApplicationException("Errore di accesso al database");
        } catch (NoSuchAlgorithmException e) {
            throw new ApplicationException("Errore");
        }
    }
