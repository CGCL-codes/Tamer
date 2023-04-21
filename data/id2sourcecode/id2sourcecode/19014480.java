    protected Object internalRead() {
        helper_ = new NativeNIOHelper(in_.getChannel());
        final SerafinAdapter inter = new SerafinAdapter();
        SerafinNewReaderInfo info = null;
        try {
            helper_.readAll(88);
            ByteBuffer bf = helper_.getBuffer();
            int tempInt = bf.getInt();
            if (tempInt != 80) {
                helper_.inverseOrder();
            }
            tempInt = bf.getInt(0);
            if (tempInt != 80) {
                analyze_.addFatalError(H2dResource.getS("La taille du premier bloc est incorrect"), (int) helper_.getCurrentPosition());
                return null;
            }
            inter.setTitre(helper_.getStingFromBuffer(80).trim());
            helper_.readData();
            bf = helper_.getBuffer();
            final int nbv1 = bf.getInt();
            if (nbv1 < 0) {
                analyze_.addFatalError(H2dResource.getS("Le nombre de variables est nul ou n�gatif"));
                return null;
            }
            final int nbv2 = bf.getInt();
            if (nbv2 > 0) {
                analyze_.addWarn(H2dResource.getS("Les variables de seconde discretisation seront ignor�es"), -1);
            }
            inter.setNbv1(nbv1);
            inter.setNbv2(nbv2);
            final String[] nomVariables = isReadOnlyTimeStep() ? null : new String[nbv1];
            final String[] uniteVariables = isReadOnlyTimeStep() ? null : new String[nbv1];
            for (int i = 0; i < nbv1; i++) {
                helper_.readData();
                if (!isReadOnlyTimeStep()) {
                    bf = helper_.getBuffer();
                    nomVariables[i] = helper_.getStingFromBuffer(16).trim();
                    uniteVariables[i] = helper_.getStingFromBuffer(16).trim();
                }
            }
            inter.setNomVariables(nomVariables);
            inter.setUniteVariables(uniteVariables);
            for (int i = 0; i < nbv2; i++) {
                helper_.skipRecord();
            }
            final int nbParam = SerafinFileFormat.IPARAM_NB;
            final int[] iparam = new int[nbParam];
            helper_.readData();
            bf = helper_.getBuffer();
            for (int i = 0; i < nbParam; i++) {
                iparam[i] = bf.getInt();
            }
            inter.setIparam(iparam);
            if (SerafinFileFormat.isIdateDefiniCommon(iparam)) {
                bf = helper_.readData();
                if (!isReadOnlyTimeStep()) {
                    final int y = bf.getInt();
                    final int m = bf.getInt();
                    final int j = bf.getInt();
                    final int h = bf.getInt();
                    final int min = bf.getInt();
                    final int s = bf.getInt();
                    final Calendar cal = Calendar.getInstance();
                    cal.set(y, m, j, h, min, s);
                    inter.setIdateInMillis(cal.getTime().getTime());
                }
            }
            bf = helper_.readData();
            final int nelem1 = bf.getInt();
            final int npoin1 = bf.getInt();
            final int nppel1 = bf.getInt();
            inter.setIdisc1(bf.getInt());
            final EfNodeMutable[] points = isReadOnlyTimeStep() ? null : new EfNodeMutable[npoin1];
            final EfElement[] elements = isReadOnlyTimeStep() ? null : new EfElement[nelem1];
            if (nbv2 > 0) {
                helper_.skipRecord();
            }
            bf = helper_.readData();
            int[] indexElem;
            if (!isReadOnlyTimeStep()) {
                for (int i = 0; i < nelem1; i++) {
                    indexElem = new int[nppel1];
                    for (int j = 0; j < nppel1; j++) {
                        indexElem[j] = bf.getInt() - 1;
                    }
                    elements[i] = new EfElement(indexElem);
                }
            }
            if (nbv2 > 0) {
                helper_.skipRecord();
            }
            int[] ipobo1 = isReadOnlyTimeStep() ? null : new int[npoin1];
            int[] ipoboInit = isReadOnlyTimeStep() ? null : new int[npoin1];
            bf = helper_.readData();
            int temp;
            int index = 0;
            if (!isReadOnlyTimeStep()) {
                for (int i = 0; i < npoin1; i++) {
                    temp = bf.getInt();
                    ipoboInit[i] = temp;
                    if (temp > 0) {
                        index++;
                        if (temp < npoin1) {
                            ipobo1[temp - 1] = i;
                        }
                    }
                }
                final int[] tempipobo1 = new int[index];
                System.arraycopy(ipobo1, 0, tempipobo1, 0, index);
                inter.setIpoboInitial(ipoboInit);
                inter.setIpoboFr(tempipobo1);
                if (progress_ != null) {
                    if (CtuluLibMessage.DEBUG) {
                        CtuluLibMessage.debug("lecture frontiere ok");
                    }
                    progress_.setProgression(10);
                }
            }
            if (nbv2 > 0) {
                helper_.skipRecord();
            }
            bf = helper_.readData();
            if (!isReadOnlyTimeStep()) {
                for (int i = 0; i < npoin1; i++) {
                    points[i] = new EfNodeMutable();
                    points[i].setX(bf.getFloat());
                }
                if (progress_ != null) {
                    if (CtuluLibMessage.DEBUG) {
                        CtuluLibMessage.debug("lecture X");
                    }
                    progress_.setProgression(20);
                }
            }
            bf = helper_.readData();
            if (!isReadOnlyTimeStep()) {
                for (int i = 0; i < npoin1; i++) {
                    points[i].setY(bf.getFloat());
                }
                if (progress_ != null) {
                    if (CtuluLibMessage.DEBUG) {
                        CtuluLibMessage.debug("lecture Y");
                    }
                    progress_.setProgression(30);
                }
            }
            if (nbv2 > 0) {
                helper_.skipRecord();
                helper_.skipRecord();
            }
            int tempo;
            if (SerafinFileFormat.isFormatEnColonneCommon(iparam)) {
                tempo = 12 + 4 * nbv1 * npoin1 + 8 * nbv1;
            } else {
                tempo = 12 + 4 * nbv1 * npoin1;
            }
            final int nbPasTempsEstime = (int) (helper_.getAvailable() / tempo);
            if (CtuluLibMessage.DEBUG) {
                CtuluLibMessage.debug("nombre pas de temps " + nbPasTempsEstime);
            }
            FuVectordouble vectorTemps = new FuVectordouble(isOnlyReadLast() ? 2 : nbPasTempsEstime + 1);
            if (nbv1 > 0) {
                final boolean isFormatColonne = SerafinFileFormat.isFormatEnColonneCommon(iparam);
                final ProgressionUpdater up = new ProgressionUpdater(progress_);
                up.setValue(7, nbPasTempsEstime, 30, 70);
                info = new SerafinNewReaderInfo(npoin1, helper_.getCurrentPosition(), file_);
                info.setColonne(isFormatColonne);
                info.setTimeEnrLength(tempo);
                info.setOrder(helper_.getOrder());
                inter.setInfo(info);
                helper_.readSequentialData();
                long nextPos;
                final FileChannel ch = helper_.getChannel();
                if (onlyReadLast_) {
                    ch.position(ch.position() + tempo * (nbPasTempsEstime - 1));
                } else if (isReadOnlyTimeStep()) {
                    ch.position(ch.position() + (tempo * (this.readTimeStepFrom_)));
                }
                while (helper_.getAvailable() > 0) {
                    nextPos = ch.position() + tempo;
                    helper_.readAll(4);
                    bf = helper_.getBuffer();
                    vectorTemps.addElement(bf.getFloat());
                    ch.position(nextPos);
                    up.majAvancement();
                }
                inter.setPasDeTemps(vectorTemps.toArray());
                vectorTemps = null;
                if (!isOnlyReadLast() && !isReadOnlyTimeStep() && inter.getTimeStepNb() != nbPasTempsEstime) {
                    analyze_.addInfo(H2dResource.getS("Nb pas de temps mal estim� (estim� {0}, lu {1})", CtuluLibString.getString(nbPasTempsEstime), CtuluLibString.getString(inter.getTimeStepNb())), 0);
                }
            }
            if (!isReadOnlyTimeStep()) {
                EfElementType type = EfElementType.getCommunType(elements[0].getPtNb());
                if (type == EfElementType.T6) type = EfElementType.T3_FOR_3D;
                inter.setMaillage(new EfGridArray(points, elements, type));
            }
        } catch (final IOException e) {
            analyze_.manageException(e);
        } finally {
            if (helper_ != null) {
                try {
                    helper_.getChannel().close();
                } catch (final IOException e1) {
                    analyze_.manageException(e1);
                }
            }
        }
        return inter;
    }
