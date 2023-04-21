    public void mouseDragged(final MouseEvent _evt) {
        if (isGele()) {
            return;
        }
        if (!enCreation_) {
            return;
        }
        final Graphics2D g = (Graphics2D) calque_.getGraphics();
        g.setXORMode(calque_.getBackground());
        final TraceLigne trace = new TraceLigne();
        trace.setCouleur(getForeground());
        trace.setTypeTrait(typeTrait_);
        final int xi = (int) pointDep_.x_;
        final int yi = (int) pointDep_.y_;
        final int xfp = (int) pointFinPrec_.x_;
        final int yfp = (int) pointFinPrec_.y_;
        final int xf = _evt.getX();
        final int yf = _evt.getY();
        switch(formeCourante_) {
            case DeForme.TRAIT:
                trace.dessineTrait(g, xi, yi, xfp, yfp);
                trace.dessineTrait(g, xi, yi, xf, yf);
                break;
            case DeForme.RECTANGLE:
                trace.dessineRectangle(g, Math.min(xi, xfp), Math.min(yi, yfp), Math.abs(xfp - xi), Math.abs(yfp - yi));
                trace.dessineRectangle(g, Math.min(xi, xf), Math.min(yi, yf), Math.abs(xf - xi), Math.abs(yf - yi));
                break;
            case DeForme.ELLIPSE:
                int xm = (xi + xfp) / 2;
                int ym = (yi + yfp) / 2;
                int vxm = (int) ((xfp - xi) * DeEllipse.C_MAGIC);
                int vym = (int) ((yfp - yi) * DeEllipse.C_MAGIC);
                trace.dessineArc(g, xm, yi, xfp, ym, vxm, 0, 0, vym);
                trace.dessineArc(g, xfp, ym, xm, yfp, 0, vym, -vxm, 0);
                trace.dessineArc(g, xm, yfp, xi, ym, -vxm, 0, 0, -vym);
                trace.dessineArc(g, xi, ym, xm, yi, 0, -vym, vxm, 0);
                xm = (xi + xf) / 2;
                ym = (yi + yf) / 2;
                vxm = (int) ((xf - xi) * DeEllipse.C_MAGIC);
                vym = (int) ((yf - yi) * DeEllipse.C_MAGIC);
                trace.dessineArc(g, xm, yi, xf, ym, vxm, 0, 0, vym);
                trace.dessineArc(g, xf, ym, xm, yf, 0, vym, -vxm, 0);
                trace.dessineArc(g, xm, yf, xi, ym, -vxm, 0, 0, -vym);
                trace.dessineArc(g, xi, ym, xm, yi, 0, -vym, vxm, 0);
                break;
            case DeForme.CARRE:
                int dx = xfp - xi;
                int dy = yfp - yi;
                int adx = Math.abs(dx);
                int ady = Math.abs(dy);
                int signx = dx > 0 ? 1 : -1;
                int signy = dy > 0 ? 1 : -1;
                if (adx < ady) {
                    dessineCarreTmp(trace, xi, yi, signx, signy, adx);
                } else {
                    dessineCarreTmp(trace, xi, yi, signx, signy, ady);
                }
                dx = xf - xi;
                dy = yf - yi;
                adx = Math.abs(dx);
                ady = Math.abs(dy);
                signx = dx > 0 ? 1 : -1;
                signy = dy > 0 ? 1 : -1;
                if (adx < ady) {
                    dessineCarreTmp(trace, xi, yi, signx, signy, adx);
                } else {
                    dessineCarreTmp(trace, xi, yi, signx, signy, ady);
                }
                break;
            case DeForme.CERCLE:
                dx = xfp - xi;
                dy = yfp - yi;
                adx = Math.abs(dx);
                ady = Math.abs(dy);
                signx = dx > 0 ? 1 : -1;
                signy = dy > 0 ? 1 : -1;
                if (adx < ady) {
                    dessineCercleTmp(trace, xi, yi, signx, signy, adx);
                } else {
                    dessineCercleTmp(trace, xi, yi, signx, signy, ady);
                }
                dx = xf - xi;
                dy = yf - yi;
                adx = Math.abs(dx);
                ady = Math.abs(dy);
                signx = dx > 0 ? 1 : -1;
                signy = dy > 0 ? 1 : -1;
                if (adx < ady) {
                    dessineCercleTmp(trace, xi, yi, signx, signy, adx);
                } else {
                    dessineCercleTmp(trace, xi, yi, signx, signy, ady);
                }
                break;
            case DeForme.COURBE_FERMEE:
            case DeForme.MAIN_LEVEE:
                final GrPoint pointFin = new GrPoint(xf, yf, 0.);
                ((DeLigneBrisee) formeCreation_).ajoute(pointFin.applique(getVersReel()));
                pointDep_ = pointFin;
                trace.dessineTrait(g, xi, yi, xfp, yfp);
                trace.dessineTrait(g, xi, yi, xf, yf);
                break;
            default:
                break;
        }
        pointFinPrec_ = new GrPoint(xf, yf, 0.);
    }
