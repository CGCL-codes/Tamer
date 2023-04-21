    @Override
    public void paintDonnees(final Graphics2D _g, final GrMorphisme _versEcran, final GrMorphisme _versReel, final GrBoite _clipReel) {
        if (getM().isWorkOnFrontierPt()) {
            super.paintDonnees(_g, _versEcran, _versReel, _clipReel);
        }
        final TrTelemacWeirModel model = getM();
        if (model.getNombre() == 0) {
            return;
        }
        Color foreground = getForeground();
        if (isAttenue()) {
            foreground = EbliLib.getAlphaColor(attenueCouleur(foreground), alpha_);
        } else if (EbliLib.isAlphaChanged(alpha_)) {
            foreground = EbliLib.getAlphaColor(foreground, alpha_);
        }
        if (tl_ == null) {
            tl_ = new TraceLigne();
            tl_.setEpaisseur(2);
            tlInterne_ = new TraceLigne();
        }
        tl_.setCouleur(foreground);
        tlInterne_.setCouleur(foreground);
        final GrBoite b = new GrBoite(new GrPoint(), new GrPoint());
        final GrSegment s = new GrSegment(new GrPoint(), new GrPoint());
        final boolean isRapide = isRapide();
        final H2dTelemacSeuilMng mng = model.getSeuils();
        TraceIcon erreurDist = null;
        for (int i = model.getNbBox() - 1; i >= 0; i--) {
            model.getBoiteForBox(i, b);
            if (traceSeuilWithBadIdx_ && !mng.getTelemacSeuil(i).isIdxValide()) {
                tl_.setCouleur(Color.RED);
                tlInterne_.setCouleur(Color.RED);
            } else {
                tl_.setCouleur(null);
                tlInterne_.setCouleur(null);
            }
            boolean first = true;
            double xo = 0, yo = 0, xe = 0, ye = 0;
            if (_clipReel.intersectXY(b)) {
                for (int j = model.getNbPointInBox(i) - 1; j >= 0; j--) {
                    model.getPoint1For(i, j, s.e_);
                    model.getPoint2For(i, j, s.o_);
                    if (_clipReel.intersectXYBoite(s)) {
                        s.autoApplique(_versEcran);
                        if (first || j == 0) {
                            tl_.dessineTrait(_g, s.o_.x_, s.o_.y_, s.e_.x_, s.e_.y_);
                            xo = s.o_.x_;
                            yo = s.o_.y_;
                            xe = s.e_.x_;
                            ye = s.e_.y_;
                        } else {
                            tlInterne_.dessineTrait(_g, s.o_.x_, s.o_.y_, s.e_.x_, s.e_.y_);
                        }
                    }
                    if (!isRapide) {
                        if (!first) {
                            model.getPoint1For(i, j, s.e_);
                            model.getPoint1For(i, j + 1, s.o_);
                            boolean traceErr = false;
                            if (pourcentageErreur_ >= 0) {
                                final H2dTelemacSeuil seuil = model.getSeuils().getTelemacSeuil(i);
                                final double d1 = seuil.getDistanceXY(true, j, mng.getGrid());
                                final double d2 = seuil.getDistanceXY(false, j, mng.getGrid());
                                double err = 100 * (d1 - d2) / d1;
                                if (err < 0) {
                                    err = -err;
                                }
                                if (err > pourcentageErreur_) {
                                    if (erreurDist == null) {
                                        erreurDist = new TraceIcon(TraceIcon.CROIX, 4);
                                        erreurDist.setCouleur(Color.RED);
                                    }
                                    traceErr = true;
                                }
                            }
                            if (_clipReel.intersectXYBoite(s)) {
                                s.autoApplique(_versEcran);
                                tl_.dessineTrait(_g, s.o_.x_, s.o_.y_, s.e_.x_, s.e_.y_);
                                if (traceErr && erreurDist != null) {
                                    final Color old = _g.getColor();
                                    erreurDist.paintIconCentre(_g, (s.o_.x_ + s.e_.x_) / 2, (s.o_.y_ + s.e_.y_) / 2);
                                    _g.setColor(old);
                                }
                            }
                            model.getPoint2For(i, j, s.e_);
                            model.getPoint2For(i, j + 1, s.o_);
                            if (_clipReel.intersectXYBoite(s)) {
                                s.autoApplique(_versEcran);
                                tl_.dessineTrait(_g, s.o_.x_, s.o_.y_, s.e_.x_, s.e_.y_);
                                if (traceErr && erreurDist != null) {
                                    final Color old = _g.getColor();
                                    erreurDist.paintIconCentre(_g, (s.o_.x_ + s.e_.x_) / 2, (s.o_.y_ + s.e_.y_) / 2);
                                    _g.setColor(old);
                                }
                            }
                        }
                        if (afficheDebFin_ && (first || j == 0)) {
                            final double x = (xe + xo) / 2;
                            final double y = (ye + yo) / 2;
                            final Color old = _g.getColor();
                            final TraceBox tb = getTraceBox();
                            if (model.isCycle(i) && first) {
                                tb.paintBox(_g, (int) x, (int) y, "1-2");
                            } else if (first) {
                                tb.paintBox(_g, (int) x, (int) y, CtuluLibString.DEUX);
                            } else {
                                tb.paintBox(_g, (int) x, (int) y, CtuluLibString.UN);
                            }
                            _g.setColor(old);
                        }
                    }
                    first = false;
                }
            }
        }
        paintTemporaire(_g);
    }
