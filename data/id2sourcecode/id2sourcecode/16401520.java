    private void actionSplit() {
        if (plageEnCours_ == null) {
            return;
        }
        final double min = plageEnCours_.getMin();
        final double max = plageEnCours_.getMax();
        final double nmilieu = (min + max) / 2;
        ((BPlage) plageEnCours_).setMax(nmilieu);
        final BPlage nplage = new BPlage(plageEnCours_);
        nplage.setMinMax(nmilieu, max);
        final int i = list_.getSelectedIndex();
        Color sup = Color.MAGENTA;
        if (i < (plages_.size() - 1)) {
            sup = ((BPlage) plages_.get(i + 1)).getCouleur();
            plages_.add(i + 1, nplage);
        } else {
            plages_.add(nplage);
        }
        if (plageEnCours_.getCouleur() == sup) {
            sup = Color.gray;
        }
        nplage.setCouleur(BPalettePlageAbstract.getCouleur(plageEnCours_.getCouleur(), sup, 0.5));
        model_.fireAdd(i + 1, i + 1);
        updatePanel();
    }
