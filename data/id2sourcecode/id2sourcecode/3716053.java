    public void join(JoinRequestI request, ObjectInputStream in, ObjectOutputStream out) {
        try {
            if (request == null) {
                logger.error("Probleme : requete invalide");
                out.writeObject(new InvalidRequest(new InvalidException()));
                return;
            } else {
                if (jeu != null && jeu.getName().equals(request.getPartieName())) {
                    logger.error("Le client fait deja partie du jeu " + request.getPartieName() + " sous le nom " + getName() + " .");
                    out.writeObject(new InvalidRequest(new YouAreAlreadyInItException()));
                    return;
                }
                Jeu j = Jeu.getJeu(request.getPartieName());
                if (j == null) {
                    logger.error("Le client voudrait se connecter a un jeu qui n'existe pas, ou plus: " + request.getPartieName());
                    out.writeObject(new InvalidRequest(new Inexistant_Game()));
                    return;
                }
                ArrayList<Client> aclient = j.getClients();
                if (aclient == null) {
                    logger.error("Le jeu auquel voudrait se connecter le client est invalide: " + request.getPartieName());
                    out.writeObject(new InvalidRequest(new InvalidException()));
                    return;
                }
                Iterator<Client> it = aclient.iterator();
                for (; it.hasNext(); ) {
                    Client c = it.next();
                    if (c.getName().equals(request.getPlayerName())) {
                        logger.error("Le jeu auquel voudrait se connecter le client possede deja un joueur du meme nom, ou le client est" + " deja connecte a cette partie. Le Jeu en question est : " + request.getPartieName());
                        out.writeObject(new InvalidRequest(new PlayerNameAlreadyUsed()));
                        return;
                    }
                }
                if (jeu != null) {
                    quit();
                }
                setName(request.getPlayerName());
                jeu = j;
                outObj = out;
                inObj = in;
                try {
                    j.addClient(this);
                } catch (TooManyPlayersException e) {
                    logger.error("Le jeu auquel voulait se connecte le client possede deja trop de joueurs.");
                    out.writeObject(new InvalidRequest(new TooManyPlayersException()));
                    return;
                }
                out.writeObject(new ValidRequest());
                logger.info("Le client " + getInetAddress() + " a l'adresse " + getPort() + " a rejoint la partie " + j.getName() + " sous le nom " + getName() + " .");
                j.start();
            }
        } catch (IOException e) {
            logger.error("Probleme de connexion.");
            return;
        } catch (NumberFormatException e) {
            logger.error("Creation de jeu impossible");
            return;
        }
    }
