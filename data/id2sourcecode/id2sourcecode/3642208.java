    private GrPoint valeur(final GrPoint _point) {
        double aa;
        double bb;
        double cc;
        double dd;
        double ee;
        double delta;
        double xpt;
        double ypt;
        double zpt;
        int ix;
        int iy;
        GrNoeud[] ndsEle;
        GrElement eleMin = null;
        int nbIter;
        EleChaine eleChaine;
        double[] xn;
        double[] yn;
        double[] zn;
        double[] dst;
        double dstMin;
        xn = new double[3];
        yn = new double[3];
        zn = new double[3];
        dst = new double[3];
        xpt = _point.x_;
        ypt = _point.y_;
        ix = (int) ((xpt - domMnx_) / lgCase_);
        iy = (int) ((ypt - domMny_) / htCase_);
        if (!(ix >= nbCaseX_ || ix < 0 || iy >= nbCaseY_ || iy < 0) && cases_[ix][iy] != null) {
            eleChaine = cases_[ix][iy];
            while (eleChaine != null) {
                ndsEle = eleChaine.element_.noeuds_;
                for (int i = 0; i < ndsEle.length; i++) {
                    final GrPoint pt = ndsEle[i].point_;
                    xn[i] = pt.x_;
                    yn[i] = pt.y_;
                    zn[i] = pt.z_;
                }
                final double dx21 = xn[1] - xn[0];
                final double dx31 = xn[2] - xn[0];
                final double dy21 = yn[1] - yn[0];
                final double dy31 = yn[2] - yn[0];
                final double a = dx21 * dy31 - dx31 * dy21;
                final double dx01 = xpt - xn[0];
                final double dy01 = ypt - yn[0];
                final double vksi = (dx01 * dy31 - dx31 * dy01) / a;
                final double veta = (dx21 * dy01 - dx01 * dy21) / a;
                if (vksi >= -1.e-5 && vksi <= 1. + 1.e-5) {
                    if (veta >= -1.e-5 && veta <= 1. + 1.e-5) {
                        final double coe1 = 1. - vksi - veta;
                        if (coe1 >= -1.e-5 && coe1 <= 1. + 1.e-5) {
                            zpt = coe1 * zn[0] + vksi * zn[1] + veta * zn[2];
                            return new GrPoint(xpt, ypt, zpt);
                        }
                    }
                }
                eleChaine = eleChaine.suivant_;
            }
            if (deltaMax_ > 1.e-2) {
                nbIter = 11;
            } else {
                nbIter = 1;
            }
            for (int j = 0; j < nbIter; j++) {
                if (j + 1 == nbIter) {
                    delta = deltaMax_;
                } else {
                    delta = j * (deltaMax_ - 1.e-2) / 10. + 1.e-2;
                }
                eleChaine = cases_[ix][iy];
                while (eleChaine != null) {
                    ndsEle = eleChaine.element_.noeuds_;
                    for (int i = 0; i < ndsEle.length; i++) {
                        final GrPoint pt = ndsEle[i].point_;
                        xn[i] = pt.x_;
                        yn[i] = pt.y_;
                        zn[i] = pt.z_;
                    }
                    for (int i = 0; i < 3; i++) {
                        aa = yn[i] - yn[(i + 1) % 3];
                        bb = xn[(i + 1) % 3] - xn[i];
                        cc = xn[i] * yn[(i + 1) % 3] - xn[(i + 1) % 3] * yn[i];
                        dst[i] = (aa * xpt + bb * ypt + cc) / Math.sqrt(aa * aa + bb * bb);
                    }
                    if ((dst[0] > -delta && dst[1] > -delta && dst[2] > -delta) || (dst[0] <= delta && dst[1] <= delta && dst[2] <= delta)) {
                        aa = yn[0] * (zn[1] - zn[2]) + yn[1] * (zn[2] - zn[0]) + yn[2] * (zn[0] - zn[1]);
                        bb = zn[0] * (xn[1] - xn[2]) + zn[1] * (xn[2] - xn[0]) + zn[2] * (xn[0] - xn[1]);
                        dd = xn[0] * (yn[1] - yn[2]) + xn[1] * (yn[2] - yn[0]) + xn[2] * (yn[0] - yn[1]);
                        ee = -(aa * xn[1] + bb * yn[1] + dd * zn[1]);
                        zpt = (-ee - aa * xpt - bb * ypt) / dd;
                        return new GrPoint(xpt, ypt, zpt);
                    }
                    eleChaine = eleChaine.suivant_;
                }
            }
        }
        if (!isZExtrapole()) {
            return new GrPoint(xpt, ypt, Double.NaN);
        }
        dstMin = Double.POSITIVE_INFINITY;
        for (ix = 0; ix < nbCaseX_; ix++) {
            for (iy = 0; iy < nbCaseY_; iy++) {
                eleChaine = cases_[ix][iy];
                while (eleChaine != null) {
                    ndsEle = eleChaine.element_.noeuds_;
                    for (int i = 0; i < ndsEle.length; i++) {
                        final GrPoint pt = ndsEle[i].point_;
                        xn[i] = pt.x_;
                        yn[i] = pt.y_;
                        zn[i] = pt.z_;
                    }
                    for (int i = 0; i < 3; i++) {
                        aa = yn[i] - yn[(i + 1) % 3];
                        bb = xn[(i + 1) % 3] - xn[i];
                        cc = xn[i] * yn[(i + 1) % 3] - xn[(i + 1) % 3] * yn[i];
                        final double d = Math.abs((aa * xpt + bb * ypt + cc) / Math.sqrt(aa * aa + bb * bb));
                        if (d < dstMin) {
                            eleMin = eleChaine.element_;
                            dstMin = d;
                        }
                    }
                    eleChaine = eleChaine.suivant_;
                }
            }
        }
        if (eleMin == null) {
            return new GrPoint(xpt, ypt, Double.NaN);
        }
        ndsEle = eleMin.noeuds_;
        for (int i = 0; i < ndsEle.length; i++) {
            final GrPoint pt = ndsEle[i].point_;
            xn[i] = pt.x_;
            yn[i] = pt.y_;
            zn[i] = pt.z_;
        }
        System.out.println("Distance min : " + dstMin);
        System.out.println("Coordonn�es des noeuds de l'�l�ment + proche :");
        System.out.println("x: " + xn[0] + ", y: " + yn[0] + ", z: " + zn[0]);
        System.out.println("x: " + xn[1] + ", y: " + yn[1] + ", z: " + zn[1]);
        System.out.println("x: " + xn[2] + ", y: " + yn[2] + ", z: " + zn[2]);
        aa = yn[0] * (zn[1] - zn[2]) + yn[1] * (zn[2] - zn[0]) + yn[2] * (zn[0] - zn[1]);
        bb = zn[0] * (xn[1] - xn[2]) + zn[1] * (xn[2] - xn[0]) + zn[2] * (xn[0] - xn[1]);
        dd = xn[0] * (yn[1] - yn[2]) + xn[1] * (yn[2] - yn[0]) + xn[2] * (yn[0] - yn[1]);
        ee = -(aa * xn[1] + bb * yn[1] + dd * zn[1]);
        zpt = (-ee - aa * xpt - bb * ypt) / dd;
        return new GrPoint(xpt, ypt, zpt);
    }
