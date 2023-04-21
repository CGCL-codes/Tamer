    public void makeWiresSignalsData() {
        messageTextLocal.setText(null);
        int chMax = rawData.getChannelsNumber();
        int posMax = rawData.getPositionsNumberSlit();
        int posHMax = rawData.getPositionsNumberHarp();
        int nSamples = rawData.getSamplesNumber();
        if (chMax * posMax <= 0) {
            messageTextLocal.setText(null);
            messageTextLocal.setText("The raw data do not exist.");
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        if (useFilter_Button.isSelected() == false && useGraphData_Button.isSelected() == true) {
            Vector cdV = GP.getAllCurveData();
            if (cdV.size() != chMax * posMax * posHMax) {
                messageTextLocal.setText(null);
                messageTextLocal.setText("You have to plot all raw data if you want to use this feature.");
                Toolkit.getDefaultToolkit().beep();
                return;
            }
        }
        int[] smplLimits = plotRawDataPanel.getLimits();
        if (smplLimits[0] > nSamples || smplLimits[0] < 1) {
            smplLimits[0] = 1;
        }
        if (smplLimits[1] < 1) {
            smplLimits[1] = 1;
        }
        if (smplLimits[1] > nSamples) {
            smplLimits[1] = nSamples;
        }
        if (smplLimits[2] < 1) {
            smplLimits[2] = 1;
        }
        if (smplLimits[2] > nSamples) {
            smplLimits[2] = nSamples;
        }
        if (smplLimits[1] > smplLimits[2]) {
            int tmp = smplLimits[1];
            smplLimits[1] = smplLimits[2];
            smplLimits[2] = tmp;
        }
        smplLimits[0] = smplLimits[0] - 1;
        smplLimits[1] = smplLimits[1] - 1;
        smplLimits[2] = smplLimits[2] - 1;
        wireSignalData.setSizeParameters(posMax, posHMax, chMax);
        setGridLimitsGP_sp(chMax, posMax);
        double val;
        double sum;
        double sum_bckg;
        for (int ip = 0; ip < posMax; ip++) {
            val = rawData.getSlitPos(ip);
            wireSignalData.setSlitPos(ip, val);
            for (int ih = 0; ih < posHMax; ih++) {
                val = rawData.getHarpPos(ip, ih);
                wireSignalData.setHarpPos(ip, ih, val);
            }
        }
        if (useFilter_Button.isSelected() == false && useGraphData_Button.isSelected() == false) {
            for (int ip = 0; ip < posMax; ip++) {
                for (int ih = 0; ih < posHMax; ih++) {
                    for (int ic = 0; ic < chMax; ic++) {
                        sum = 0.;
                        sum_bckg = 0.;
                        for (int is = 0; is < smplLimits[0]; is++) {
                            sum_bckg += rawData.getValue(is, ip, ih, ic);
                        }
                        for (int is = smplLimits[1]; is <= smplLimits[2]; is++) {
                            sum += rawData.getValue(is, ip, ih, ic);
                        }
                        if (smplLimits[0] > 0) {
                            sum_bckg /= smplLimits[0];
                        }
                        if (smplLimits[2] - smplLimits[1] > 0) {
                            sum /= (smplLimits[2] - smplLimits[1]);
                        }
                        sum = sum_bckg - sum;
                        wireSignalData.setValue(ip, ih, ic, sum);
                    }
                }
            }
        }
        if (useFilter_Button.isSelected() == true && useGraphData_Button.isSelected() == false) {
            for (int ip = 0; ip < posMax; ip++) {
                for (int ih = 0; ih < posHMax; ih++) {
                    for (int ic = 0; ic < chMax; ic++) {
                        tmp_CurveData.clear();
                        for (int is = 0; is < nSamples; is++) {
                            val = rawData.getValue(is, ip, ih, ic);
                            tmp_CurveData.addPoint(is, val);
                        }
                        filterRawDataPanel.filterCurveData(tmp_CurveData);
                        sum = 0.;
                        sum_bckg = 0.;
                        for (int is = 0; is < smplLimits[0]; is++) {
                            sum_bckg += tmp_CurveData.getY(is);
                        }
                        for (int is = smplLimits[1]; is <= smplLimits[2]; is++) {
                            sum += tmp_CurveData.getY(is);
                        }
                        if (smplLimits[0] > 0) {
                            sum_bckg /= smplLimits[0];
                        }
                        if (smplLimits[2] - smplLimits[1] > 0) {
                            sum /= (smplLimits[2] - smplLimits[1]);
                        }
                        sum = sum_bckg - sum;
                        wireSignalData.setValue(ip, ih, ic, sum);
                    }
                }
            }
        }
        if (useFilter_Button.isSelected() == false && useGraphData_Button.isSelected() == true) {
            Vector cdV = GP.getAllCurveData();
            if (cdV.size() != chMax * posMax * posHMax) {
                messageTextLocal.setText(null);
                messageTextLocal.setText("You have to plot all raw data if you want to use this feature.");
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            int ind;
            CurveData cd = null;
            for (int ip = 0; ip < posMax; ip++) {
                for (int ih = 0; ih < posHMax; ih++) {
                    for (int ic = 0; ic < chMax; ic++) {
                        ind = ip + posMax * ih + posMax * posHMax * ic;
                        cd = (CurveData) cdV.get(ind);
                        sum = 0.;
                        sum_bckg = 0.;
                        for (int is = 0; is < smplLimits[0]; is++) {
                            sum_bckg += cd.getY(is);
                        }
                        for (int is = smplLimits[1]; is <= smplLimits[2]; is++) {
                            sum += cd.getY(is);
                        }
                        if (smplLimits[0] > 0) {
                            sum_bckg /= smplLimits[0];
                        }
                        if (smplLimits[2] - smplLimits[1] > 0) {
                            sum /= (smplLimits[2] - smplLimits[1]);
                        }
                        sum = sum_bckg - sum;
                        wireSignalData.setValue(ip, ih, ic, sum);
                    }
                }
            }
        }
        double maxZ = wireSignalData.getPlotData(0).getMaxZ();
        for (int ih = 1; ih < posHMax; ih++) {
            if (maxZ < wireSignalData.getPlotData(ih).getMaxZ()) {
                maxZ = wireSignalData.getPlotData(ih).getMaxZ();
            }
        }
        if (maxZ > 0.) {
            for (int ih = 0; ih < posHMax; ih++) {
            }
        }
        for (int ih = 0; ih < posHMax; ih++) {
            wireSignalData.getPlotData(ih).calcMaxMinZ();
        }
        wireSignalData.memorizeData3D();
        int posHarp = ((Integer) useHarpPos_Spinner.getValue()).intValue();
        wireSignalData.getPlotData(posHarp - 1).setColorGenerator(colorGen_sp);
    }
