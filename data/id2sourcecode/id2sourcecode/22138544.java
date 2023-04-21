    public boolean deplacement(Joueur joueur, Case caseDepart, Case caseCible) {
        boolean deplacementPossible = false;
        if (cD.JoueurPeutJouerPiece(caseDepart, joueur)) {
            if (this.deplacementMax == 0) {
                deplacementPossible = cD.deplacementPossible(caseDepart, caseCible);
                if (deplacementPossible) {
                    Piece piece = caseDepart.getPiece();
                    caseDepart.setPiece(null);
                    caseDepart.setEtat(0);
                    caseCible.setPiece(piece);
                    caseCible.setEtat(1);
                    damier.getNewDamesNoires();
                    damier.getNewDamesBlanches();
                    setChanged();
                    notifyObservers();
                }
            } else {
                Position positionCaseDepart = caseDepart.getPosition();
                boolean caseDansCasesJouables = false;
                Iterator<Case> itrCase = this.getCasesJouables().iterator();
                while (itrCase.hasNext()) {
                    Case maCase = itrCase.next();
                    if (positionCaseDepart.comparePosition(maCase.getPositionX(), maCase.getPositionY())) {
                        caseDansCasesJouables = true;
                        break;
                    }
                }
                if (caseDansCasesJouables) {
                    int xCaseDepart = caseDepart.getPositionX();
                    int yCaseDepart = caseDepart.getPositionY();
                    int xCaseCible = caseCible.getPositionX();
                    int yCaseCible = caseCible.getPositionY();
                    Noeud noeud = null;
                    int xNoeud;
                    int yNoeud;
                    Iterator<Noeud> itrNoeud = this.ListeChemins.iterator();
                    while (itrNoeud.hasNext()) {
                        noeud = itrNoeud.next();
                        xNoeud = noeud.getX();
                        yNoeud = noeud.getY();
                        if (xCaseDepart == xNoeud && yCaseDepart == yNoeud) break;
                    }
                    ArrayList<int[][]> plusLongChemin = new ArrayList<int[][]>();
                    noeud.getPlusLongChemins(plusLongChemin, noeud.getProfondeurArbre(), noeud);
                    Iterator<int[][]> itrTableauInt = plusLongChemin.iterator();
                    int[][] tabCoordonnees;
                    while (itrTableauInt.hasNext()) {
                        tabCoordonnees = itrTableauInt.next();
                        if (tabCoordonnees[1][0] == xCaseCible && tabCoordonnees[1][1] == yCaseCible) {
                            if (this.deplacementMax == tabCoordonnees.length - 1) deplacementPossible = true; else deplacementPossible = false;
                            break;
                        }
                    }
                    if (deplacementPossible) {
                        int xCaseInter = (xCaseDepart + xCaseCible) / 2;
                        int yCaseInter = (yCaseDepart + yCaseCible) / 2;
                        Case caseInter = this.damier.getCase(xCaseInter, yCaseInter);
                        Piece piece = caseDepart.getPiece();
                        caseDepart.setPiece(null);
                        caseDepart.setEtat(0);
                        caseInter.setPiece(null);
                        caseInter.setEtat(0);
                        caseCible.setPiece(piece);
                        caseCible.setEtat(1);
                        damier.getNewDamesNoires();
                        damier.getNewDamesBlanches();
                        setChanged();
                        notifyObservers();
                    }
                }
            }
        }
        return deplacementPossible;
    }
