    public void open(OpenRequestI request, ObjectInputStream in, ObjectOutputStream out) {
        try {
            if (request == null) {
                logger.error("Probleme : requete invalide");
                out.writeObject(new InvalidRequest(new InvalidException()));
                return;
            } else {
                int taille = request.getPlateauTaille();
                if (taille < 7) {
                    logger.warn("Mauvaise taille de plateau de jeu demandee : " + taille);
                    taille = 7;
                } else logger.info("Taille du plateau : " + taille);
                int nbGaranties = request.getNbGaranties();
                if (nbGaranties < 0) {
                    logger.warn("Mauvais nombre de garantie demande : " + nbGaranties);
                    nbGaranties = 0;
                } else logger.info("Nombre de Garanties: " + nbGaranties);
                int nbJoueurs = request.getNbJoueurs();
                if (nbJoueurs < 2) {
                    logger.warn("Mauvais nombre de joueurs demande : " + nbGaranties);
                    nbJoueurs = 2;
                } else logger.info("Nombre de Joueurs: " + nbJoueurs);
                setName(request.getPlayerName());
                logger.info("Le joueur administrateur se nomme : " + request.getPlayerName());
                Jeu j = null;
                try {
                    j = Jeu.getNewJeu(request.getPartieName(), this, taille, nbGaranties, nbJoueurs);
                    if (j == null || j.getPartie() == null) {
                        logger.error("Creation de jeu impossible : ce nom de jeu existe deja");
                        out.writeObject(new InvalidRequest(new AlreadyExistsException()));
                        return;
                    } else {
                        if (this.jeu != null) this.jeu.close();
                        this.jeu = j;
                        try {
                            j.addClient(this);
                        } catch (TooManyPlayersException e) {
                            logger.error("Le jeu auquel voulait se connecte le client possede deja trop de joueurs.");
                            out.writeObject(new InvalidRequest(new TooManyPlayersException()));
                            return;
                        }
                        out.writeObject(new ValidRequest());
                        outObj = out;
                        inObj = in;
                        return;
                    }
                } catch (Exception e) {
                    logger.error("Creation de jeu impossible");
                    out.writeObject(new InvalidRequest(new Exception()));
                    return;
                }
            }
        } catch (IOException e) {
            logger.error("Probleme de connexion.");
            return;
        } catch (NumberFormatException e) {
            logger.error("Creation de jeu impossible");
            return;
        }
    }
