    @Override
    public void run() {
        Logger logger = Logger.getLogger(JoueurPartieServeur.class.getName());
        init();
        this._serveur.isReady();
        try {
            logger.log(Level.INFO, "Serveur is ready");
            this.write(0);
            while (!this._serveur.isFinish()) {
                int command = this._inputStream.read();
                ObjectInputStream in = null;
                switch(command) {
                    case MessageType.ASK_PLATEAU:
                        logger.log(Level.INFO, "ASK PLATEAU");
                        int cpt = 0;
                        while (_tourJoueur != this._serveur.getCurrentTour() || this._tourJoueur == 0) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(JoueurPartieServeur.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            cpt++;
                            if (cpt == Integer.MAX_VALUE) System.exit(1000);
                        }
                        this.write(this._serveur.getPlateau());
                        break;
                    case MessageType.END_TURN:
                        logger.log(Level.INFO, "END TURN");
                        in = new ObjectInputStream(this._inputStream);
                        this._serveur.play((ListOrdre) in.readObject());
                        _tourJoueur++;
                        break;
                    case MessageType.HAVE_WIN:
                        logger.log(Level.INFO, "HAVE WIN");
                        in = new ObjectInputStream(this._inputStream);
                        this.write(this._serveur.haveWin((Equipe) in.readObject()));
                        break;
                    case MessageType.HAVE_LOSE:
                        logger.log(Level.INFO, "HAVE LOSE");
                        in = new ObjectInputStream(this._inputStream);
                        this.write(this._serveur.haveLose((Equipe) in.readObject()));
                        break;
                    case MessageType.TURN:
                        logger.log(Level.INFO, "TURN");
                        this.write(this._serveur.getCurrentTour());
                        break;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JoueurPartieServeur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JoueurPartieServeur.class.getName()).log(Level.SEVERE, "FATAL ERROR", ex);
            System.exit(10);
        }
    }
