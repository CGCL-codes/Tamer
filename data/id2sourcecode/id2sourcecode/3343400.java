    private void enregistreData() {
        DaoDevise daoDevise = new DaoDevise();
        int idEcriture = 0;
        int idjou = 1;
        Ecriture uneEcriture = ecritureDejaPassee(ladate);
        if (uneEcriture != null) {
            try {
                daoEcritureLigne.supprimeEcritureLigneByIdEcriture(uneEcriture.getIdEcriture());
            } catch (BusinessException be) {
                Erreurs.Warning(be);
            }
            idEcriture = uneEcriture.getIdEcriture();
        } else {
            try {
                idEcriture = daoEcriture.getNewIdEcriture();
            } catch (BusinessException be) {
                Erreurs.Warning(be);
            }
            try {
                daoEcriture.ajouteEcriture(new Ecriture(idEcriture, new SimpleDateFormat("yyyy-MM-dd").format(ladate), daoDevise.getDeviseById(ActionConstants.ATT_PARAM_IDDEV).getNom_devise(), "b", 0, false, ActionConstants.ATT_PARAM_IDEXE, idjou, ActionConstants.ATT_PARAM_IDDEV, 0, ""));
            } catch (BusinessException be) {
                Erreurs.Warning(be);
            }
        }
        List<Categorie> listCategorieNat = getListCategorie("Nature");
        BigDecimal bdMttNature = new BigDecimal(0);
        for (Categorie uneCat : listCategorieNat) {
            ChampMonetaire chmMtt = null;
            for (int i = 0; i < panelNature.getComponentCount(); i++) {
                if (panelNature.getComponent(i) instanceof JLabel) {
                    JLabel unLabel = (JLabel) panelNature.getComponent(i);
                    if (unLabel.getText().equals(uneCat.getLibelle())) {
                        int a = i + 1;
                        chmMtt = (ChampMonetaire) panelNature.getComponent(a);
                    }
                }
            }
            if (chmMtt != null && !chmMtt.getText().equals("0,00")) {
                Number nombre = 0;
                try {
                    nombre = format.parse(chmMtt.getText().replace(".", ","));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                bdMttNature = new BigDecimal(nombre.toString());
                try {
                    EcritureLigne uneEcritureLigne = new EcritureLigne(daoEcritureLigne.getNewIdEcritureLigne(), "Ventes comptant pour le " + new SimpleDateFormat("dd-MM-yyyy").format(ladate), bdMttNature, false, "+", "0000", new BigDecimal(0), false, new BigDecimal(1), uneCat.getIdCom(), ActionConstants.ATT_PARAM_IDTVAZ, idEcriture);
                    daoEcritureLigne.ajouteEcritureLigne(uneEcritureLigne);
                } catch (BusinessException be) {
                    Erreurs.Warning(be);
                }
            }
        }
        List<Categorie> listCategorieTyp = getListCategorie("Type");
        BigDecimal bdMttType = new BigDecimal(0);
        for (Categorie uneCat : listCategorieTyp) {
            ChampMonetaire chmMtt = null;
            for (int i = 0; i < panelType.getComponentCount(); i++) {
                if (panelType.getComponent(i) instanceof JLabel) {
                    JLabel unLabel = (JLabel) panelType.getComponent(i);
                    if (unLabel.getText().equals(uneCat.getLibelle())) {
                        int a = i + 1;
                        chmMtt = (ChampMonetaire) panelType.getComponent(a);
                    }
                }
            }
            if (chmMtt != null && !chmMtt.getText().equals("0,00")) {
                Number nombre = 0;
                try {
                    nombre = format.parse(chmMtt.getText().replace(".", ","));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                bdMttType = new BigDecimal(nombre.toString());
                try {
                    EcritureLigne uneEcritureLigne = new EcritureLigne(daoEcritureLigne.getNewIdEcritureLigne(), "Ventes comptant pour le " + new SimpleDateFormat("dd-MM-yyyy").format(ladate), bdMttType, true, "+", "0000", new BigDecimal(0), false, new BigDecimal(1), uneCat.getIdCom(), ActionConstants.ATT_PARAM_IDTVAZ, idEcriture);
                    daoEcritureLigne.ajouteEcritureLigne(uneEcritureLigne);
                } catch (BusinessException be) {
                    Erreurs.Warning(be);
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Ecriture Enregistr�e");
        updatePage(ladate);
    }
