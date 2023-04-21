    public static H2dRefluxBcManager init(final H2dRefluxSourceInterface _s, final CtuluAnalyze _analyze) {
        final EfFrontierInterface frontieres = _s.getGrid().getFrontiers();
        int ptIdxGlobal;
        final H2dRefluxBcManager r = new H2dRefluxBcManager(_s.getGrid());
        final int n = frontieres.getNbFrontier();
        final RefluxMiddleFrontier[] bordByFrontier = new RefluxMiddleFrontier[n];
        final H2dRefluxBordIndexGeneral[] bordSpecifies = _s.getBords();
        final H2dRefluxBoundaryCondition[] cl3Temp = new H2dRefluxBoundaryCondition[3];
        for (int i = 0; i < n; i++) {
            final int nbPt = frontieres.getNbPt(i);
            final H2dRefluxBoundaryCondition[] cls = new H2dRefluxBoundaryCondition[nbPt];
            for (int j = 1; j < nbPt; j += 2) {
                ptIdxGlobal = frontieres.getIdxGlobal(i, j - 1);
                H2dRefluxBoundaryCondition cl = _s.getConditionLimite(ptIdxGlobal);
                if (cl == null) {
                    if (_analyze != null) {
                        _analyze.addError(H2dResource.getS("Pas de cl pour le point de bord {0}", CtuluLibString.getString(ptIdxGlobal)));
                    }
                    final H2dRefluxBoundaryCondition clN = new H2dRefluxBoundaryCondition();
                    clN.setIndexPt(ptIdxGlobal);
                    cls[j - 1] = clN;
                } else {
                    cl.initUsedEvol();
                    cls[j - 1] = cl;
                }
                cl3Temp[0] = cls[j - 1];
                if (j < (nbPt - 1)) {
                    ptIdxGlobal = frontieres.getIdxGlobal(i, j + 1);
                    cl = _s.getConditionLimite(ptIdxGlobal);
                    if (cl == null) {
                        if (_analyze != null) {
                            _analyze.addError(H2dResource.getS("Pas de cl pour le point de bord {0}", CtuluLibString.getString(ptIdxGlobal)));
                        }
                        final H2dRefluxBoundaryConditionMutable clN = new H2dRefluxBoundaryConditionMutable();
                        clN.setIndexPt(ptIdxGlobal);
                        cls[j + 1] = clN;
                    } else {
                        cl.initUsedEvol();
                        cls[j + 1] = cl;
                    }
                    cl3Temp[2] = cls[j + 1];
                }
                ptIdxGlobal = frontieres.getIdxGlobal(i, j);
                final H2dRefluxBordIndexGeneral bord = bordSpecifies == null ? null : H2dRefluxBordIndexGeneral.findBordWithIndex(ptIdxGlobal, bordSpecifies);
                H2dBoundaryType bordType = H2dRefluxBoundaryType.SOLIDE;
                cl = _s.getConditionLimite(ptIdxGlobal);
                if (cl == null) {
                    if (_analyze != null) {
                        _analyze.addFatalError(H2dResource.getS("Pas de cl pour le point de bord {0}", CtuluLibString.getString(ptIdxGlobal)));
                    }
                    return null;
                }
                if (bord == null) {
                    cl3Temp[1] = cl;
                    if (isOuvert(cl3Temp)) {
                        bordType = H2dRefluxBoundaryType.LIQUIDE;
                    }
                } else {
                    bordType = bord.getBordType();
                }
                if (bord != null && bordType == H2dRefluxBoundaryType.SOLIDE_FROTTEMENT) {
                    cl = new H2dRefluxBoundaryConditionMiddleFriction(cl);
                    cl.setValue(H2dVariableType.RUGOSITE, bord.getRugositeType(), bord.getRugosite(), bord.getRugositeTransitoireCourbe());
                } else {
                    cl = new H2dRefluxBoundaryConditionMiddle(cl, bordType);
                    if (bordType == H2dRefluxBoundaryType.LIQUIDE && cl.getHType() == H2dBcType.LIBRE && cl3Temp[0].getHType() != H2dBcType.LIBRE && cl3Temp[2].getHType() != H2dBcType.LIBRE) {
                        if (cl3Temp[0].getHType() == H2dBcType.PERMANENT && cl3Temp[2].getHType() == H2dBcType.PERMANENT) {
                            cl.setHTypePermanent((cl3Temp[0].getH() + cl3Temp[2].getH()) / 2);
                        } else {
                            if (cl3Temp[0].getHType() == H2dBcType.PERMANENT) {
                                cl.setH(cl3Temp[0].getH());
                            } else {
                                cl.setHTransitoire(cl3Temp[0].getHTransitoireCourbe());
                            }
                        }
                    }
                }
                cl.initUsedEvol();
                cl.setIndexPt(ptIdxGlobal);
                cls[j] = cl;
            }
            bordByFrontier[i] = r.createRefluxMiddleFrontier(i, cls);
        }
        r.bcFrontier_ = bordByFrontier;
        return r;
    }
