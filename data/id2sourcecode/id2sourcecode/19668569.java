    private void makeEmittanceData() {
        alphaEm_Text.setText(null);
        betaEm_Text.setText(null);
        rmsEm_Text.setText(null);
        int nCh = wireSignalData.getChannelsNumber();
        int nPS = wireSignalData.getPositionsNumberSlit();
        int nPH = wireSignalData.getPositionsNumberHarp();
        if (nCh * nPS <= 1) {
            return;
        }
        emittanceRawData.resize(nPS, nCh * nPH);
        double val = 0.;
        double pos = 0.;
        double posH = 0.;
        double angl = 0.;
        if (sortObjects.length != nCh * nPH) {
            sortObjects = new SortObject[nCh * nPH];
            for (int i = 0; i < nCh * nPH; i++) {
                sortObjects[i] = new SortObject();
            }
        }
        int i_count = 0;
        for (int ip = 0; ip < nPS; ip++) {
            pos = wireSignalData.getSlitPos(ip);
            i_count = 0;
            for (int ih = 0; ih < nPH; ih++) {
                posH = wireSignalData.getHarpPos(ip, ih);
                for (int ic = 0; ic < nCh; ic++) {
                    angl = (posH - wireSignalData.getSlitPos(ip) - wireStep * ic) / (harpSlitDist * 10.0);
                    val = wireSignalData.getValue(ip, ih, ic);
                    angl = angl * 1000.0;
                    sortObjects[i_count].setParam(val, angl);
                    i_count++;
                }
            }
            Arrays.sort(sortObjects);
            for (int i = 0; i < nPH * nCh; i++) {
                angl = sortObjects[i].getAngle();
                val = sortObjects[i].getValue();
                emittanceRawData.setRawData(ip, i, pos, angl, val);
            }
        }
        emittanceRawData.setInitialized(true);
        emittance3D.setSize(emSizeX, emSizeY);
        emittance3Da.setSize(emSizeX, emSizeY);
        emittanceRawData.makeColorSurfaceData(emittance3D);
        emittanceRawData.makeColorSurfaceData(emittance3Da);
        int nX = emittance3D.getSizeX();
        int nY = emittance3D.getSizeY();
        makeEmittanceData(emittance3D);
        makeEmittanceData(emittance3Da);
        if (debug_gauss_generation) {
            makeGaussianEmittance(0.1 / gammaBeta, 1.0, 2.0, emittance3D);
            makeGaussianEmittance(0.1 / gammaBeta, 1.0, 2.0, emittance3Da);
        }
        emittance3D.calcMaxMinZ();
        emittance3Da.calcMaxMinZ();
        double val_max = threshold * emittance3D.getMaxZ() / 100.0;
        for (int ix = 0; ix < nX; ix++) {
            for (int iy = 0; iy < nY; iy++) {
                val = emittance3D.getValue(ix, iy);
                if (val < val_max) {
                    emittance3D.setValue(ix, iy, 0.);
                }
            }
        }
        emittance3D.calcMaxMinZ();
        double posMin = emittance3D.getMinX();
        double posMax = emittance3D.getMaxX();
        posMax = Math.max(Math.abs(posMax), Math.abs(posMin));
        double anglMin = emittance3D.getMinY();
        double anglMax = emittance3D.getMaxY();
        anglMax = Math.max(Math.abs(anglMax), Math.abs(anglMin));
        double stepPos = 5.0;
        double stepAngl = 5.0;
        int nStepX = (int) (posMax / stepPos);
        nStepX += 1;
        int nStepY = (int) (anglMax / stepAngl);
        nStepY += 1;
        double em_rms_x0 = 0;
        double nSum = 0.;
        double x_avg = 0.;
        double xp_avg = 0.;
        for (int ix = 0; ix < nX; ix++) {
            pos = emittance3D.getX(ix);
            for (int iy = 0; iy < nY; iy++) {
                angl = emittance3D.getY(iy);
                val = emittance3D.getValue(ix, iy);
                nSum += val;
                x_avg += val * pos;
                xp_avg += val * angl;
            }
        }
        if (nSum == 0.) {
            nSum = 1.0;
        }
        x_avg /= nSum;
        xp_avg /= nSum;
        double x2_avg = 0.;
        double xp2_avg = 0.;
        double x_xp_avg = 0.;
        for (int ix = 0; ix < nX; ix++) {
            pos = emittance3D.getX(ix) - x_avg;
            for (int iy = 0; iy < nY; iy++) {
                angl = emittance3D.getY(iy) - xp_avg;
                val = emittance3D.getValue(ix, iy);
                x2_avg += val * pos * pos;
                xp2_avg += val * angl * angl;
                x_xp_avg += val * angl * pos;
            }
        }
        x2_avg /= nSum;
        xp2_avg /= nSum;
        x_xp_avg /= nSum;
        em_rms_x0 = Math.sqrt(Math.abs(x2_avg * xp2_avg - x_xp_avg * x_xp_avg));
        if (x2_avg * xp2_avg - x_xp_avg * x_xp_avg < 0.) {
            em_rms_x0 = -em_rms_x0;
        }
        double em_rms_x0_n = em_rms_x0 * gammaBeta;
        double alpha = -x_xp_avg / em_rms_x0;
        double beta = x2_avg / em_rms_x0;
        alphaEm_Text.setValue(alpha);
        betaEm_Text.setValue(beta);
        rmsEm_Text.setValue(em_rms_x0_n);
        alphaEm_Text.setBackground(Color.white);
        betaEm_Text.setBackground(Color.white);
        rmsEm_Text.setBackground(Color.white);
        if (nSum != 0.) {
            emittance3D.multiplyBy(1.0 / nSum);
        }
        posCenterX_Text.setValue(x_avg);
        posCenterXP_Text.setValue(xp_avg);
        posCenterX_Text.setBackground(Color.white);
        posCenterXP_Text.setBackground(Color.white);
    }
