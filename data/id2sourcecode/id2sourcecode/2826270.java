    protected boolean makeStoredEnergyAnalysis() {
        analysisReady = false;
        ampVsTimeGD.removeAllPoints();
        ampVsTimeLogGD.removeAllPoints();
        ampVsTimeLogFitGD.removeAllPoints();
        rawCD.clear();
        integrationStart = 0.;
        resultsText.setText(null);
        fields[5] = 0.;
        fieldStrs[5] = " --- ";
        fieldStrsHTML[5] = "<html><body><font color= RED>" + fieldStrs[5] + "</font></body></html>";
        fieldsChanged = true;
        uc.update();
        Channel ampPV = ChannelFactory.defaultFactory().getChannel(ampPV_name);
        Channel timPV = ChannelFactory.defaultFactory().getChannel(timPV_name);
        Channel rawPV = ChannelFactory.defaultFactory().getChannel(rawPV_name);
        double pulseEnd = pulseEndPV.getValue();
        double rate = ratePV.getValue();
        double cavV = cavVPV.getValue();
        double len = lenPV.getValue();
        double loss = adcLossPV.getValue();
        try {
            double[] ampArr = ampPV.getArrDbl();
            double[] timArr = timPV.getArrDbl();
            int nP = Math.min(ampArr.length, timArr.length);
            if (nP < 3) {
                resultsText.append("Unable to connect channels:");
                resultsText.append(System.getProperty("line.separator"));
                resultsText.append(ampPV_name);
                resultsText.append(System.getProperty("line.separator"));
                resultsText.append(timPV_name);
                resultsText.append(System.getProperty("line.separator"));
                return analysisReady;
            }
            double st = timArr[2] - timArr[1];
            for (int i = 0; i < nP; i++) {
                if (timArr[i] > (pulseEnd + 1.0) && i < Math.floor(pulseEnd + 1.0 + len / st)) {
                    double amp = ampArr[i];
                    double t = timArr[i] - (pulseEnd + 1.0);
                    ampVsTimeGD.addPoint(t, amp);
                }
            }
        } catch (ConnectionException exp) {
            resultsText.append("Unable to connect channels:");
            resultsText.append(System.getProperty("line.separator"));
            resultsText.append(ampPV_name);
            resultsText.append(System.getProperty("line.separator"));
            resultsText.append(timPV_name);
            resultsText.append(System.getProperty("line.separator"));
            return analysisReady;
        } catch (GetException exp) {
            resultsText.append("Unable to connect channels:");
            resultsText.append(System.getProperty("line.separator"));
            resultsText.append(ampPV_name);
            resultsText.append(System.getProperty("line.separator"));
            resultsText.append(timPV_name);
            resultsText.append(System.getProperty("line.separator"));
            return analysisReady;
        }
        for (int i = 0; i < ampVsTimeGD.getNumbOfPoints(); i++) {
            double val = ampVsTimeGD.getY(i);
            val = Math.abs(val);
            if (val != 0.) {
                val = Math.log(val);
                ampVsTimeLogGD.addPoint(ampVsTimeGD.getX(i), val);
            }
        }
        int order = 1;
        double[][] fitCoeff = GraphDataOperations.polynomialFit(ampVsTimeLogGD, ampVsTimeLogGD.getMinX(), ampVsTimeLogGD.getMaxX(), order);
        for (int i = 0; i < ampVsTimeLogGD.getNumbOfPoints(); i++) {
            double x = ampVsTimeLogGD.getX(i);
            double val = fitCoeff[0][0] + x * fitCoeff[0][1];
            ampVsTimeLogFitGD.addPoint(x, val);
        }
        double tau = -1 / fitCoeff[0][1];
        double tau_err = Math.abs(tau * fitCoeff[1][1] / fitCoeff[0][1]);
        double Q = 2 * 3.1415926 * 805.0 * tau / 2;
        double bw_kHz = (805 / Q) * 1000.0 / 2;
        fitCoeff[0][0] = Math.exp(fitCoeff[0][0]);
        fitCoeff[1][0] = fitCoeff[0][0] * fitCoeff[1][0];
        resultsText.append("===Field_WfA Fitting===");
        resultsText.append(System.getProperty("line.separator"));
        resultsText.append("cavV= " + fmt.format(cavV) + " [MV/m]");
        resultsText.append(System.getProperty("line.separator"));
        resultsText.append("Q= " + fmt.format(Q));
        resultsText.append(System.getProperty("line.separator"));
        resultsText.append("BW= " + fmt.format(bw_kHz) + " [kHz]");
        resultsText.append(System.getProperty("line.separator"));
        resultsText.append("wf(t) = A0*exp(-t/tau) ");
        resultsText.append(System.getProperty("line.separator"));
        resultsText.append("A0=" + fmt.format(fitCoeff[0][0]) + " +- " + fmt.format(fitCoeff[1][0]));
        resultsText.append(System.getProperty("line.separator"));
        resultsText.append("tau[us]=" + fmt.format(tau) + " +- " + fmt.format(tau_err));
        resultsText.append(System.getProperty("line.separator"));
        resultsText.append("=======================");
        resultsText.append(System.getProperty("line.separator"));
        double bw = 2 * bw_kHz / 1000.0;
        double Ql = 805 / bw;
        tau = 1.0 / (2 * Math.PI * bw);
        try {
            switchHBOPV.getChannel().putVal(3);
        } catch (ConnectionException exp) {
            resultsText.append("Unable to set PV:" + switchHBOPV.getChannelName());
            return analysisReady;
        } catch (PutException exp) {
            resultsText.append("Unable to set PV:" + switchHBOPV.getChannelName());
            return analysisReady;
        }
        double[] rawArr = null;
        try {
            rawArr = rawPV.getArrDbl();
            for (int i = 0; i < rawArr.length; i++) {
                rawCD.addPoint(2.0 * i, rawArr[i]);
            }
        } catch (ConnectionException exp) {
            resultsText.append("Unable to connect channel:");
            resultsText.append(System.getProperty("line.separator"));
            resultsText.append(rawPV_name);
            resultsText.append(System.getProperty("line.separator"));
            return analysisReady;
        } catch (GetException exp) {
            resultsText.append("Unable to connect channel:");
            resultsText.append(System.getProperty("line.separator"));
            resultsText.append(rawPV_name);
            resultsText.append(System.getProperty("line.separator"));
            return analysisReady;
        }
        int nIter = 2;
        for (int iii = 0; iii < nIter; iii++) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException exp) {
                return analysisReady;
            }
            try {
                rawArr = rawPV.getArrDbl();
                for (int i = 0; i < rawArr.length; i++) {
                    double val = rawCD.getY(i);
                    rawCD.setPoint(i, rawCD.getX(i), val + rawArr[i]);
                }
            } catch (ConnectionException exp) {
                resultsText.append("Unable to connect channel:");
                resultsText.append(System.getProperty("line.separator"));
                resultsText.append(rawPV_name);
                resultsText.append(System.getProperty("line.separator"));
                return analysisReady;
            } catch (GetException exp) {
                resultsText.append("Unable to connect channel:");
                resultsText.append(System.getProperty("line.separator"));
                resultsText.append(rawPV_name);
                resultsText.append(System.getProperty("line.separator"));
                return analysisReady;
            }
        }
        for (int i = 0; i < rawArr.length; i++) {
            double val = rawCD.getY(i) / (nIter + 1);
            val = val * 0.0625 - 52.2;
            val = Math.pow(10., (val - 60 + loss) / 10.);
            rawCD.setPoint(i, rawCD.getX(i), val);
        }
        double storedEnergy = 0.;
        int max_index = (int) Math.floor(0.9 * pulseEnd / 2.0);
        int nZero = Math.min(max_index + 1, rawCD.getSize());
        for (int i = 0; i < nZero; i++) {
            rawCD.setPoint(i, rawCD.getX(i), 0.);
        }
        double p0 = -Double.MAX_VALUE;
        int nP = rawCD.getSize();
        int time_0_ind = 0;
        for (int i = 0; i < nP; i++) {
            if (p0 <= rawCD.getY(i)) {
                p0 = rawCD.getY(i);
            }
        }
        for (int i = 0; i < nP; i++) {
            if (p0 == rawCD.getY(i)) {
                time_0_ind = i;
                break;
            }
        }
        rawCD.findMinMax();
        integrationStart = time_0_ind * 2.0;
        double U = p0 * tau;
        U = U / 1000.;
        double Un = 0.;
        int i_start = time_0_ind;
        i_start = Math.max(0, i_start);
        int i_stop = nP - 1;
        for (int i = i_start; i <= i_stop; i++) {
            Un += rawCD.getY(i);
        }
        if (i_start < i_stop) {
            Un -= 0.5 * rawCD.getY(i_start);
        }
        Un -= 0.5 * rawCD.getY(i_stop);
        Un *= (2.0 / 1000.0);
        stored_energy = Un;
        resultsText.append("Un[J] = " + fmt.format(Un));
        resultsText.append(System.getProperty("line.separator"));
        resultsText.append("U[J] = " + fmt.format(U));
        resultsText.append(System.getProperty("line.separator"));
        resultsText.append("=======================");
        resultsText.append(System.getProperty("line.separator"));
        resultsText.append("ADCSnap0_Pwr = " + fmt.format(powerMPVs[2].getValue()));
        resultsText.append(System.getProperty("line.separator"));
        resultsText.append("ADCSnap1_Pwr = " + fmt.format(powerMPVs[0].getValue()));
        resultsText.append(System.getProperty("line.separator"));
        resultsText.append("ADCSnap2_Pwr = " + fmt.format(powerMPVs[1].getValue()));
        resultsText.append(System.getProperty("line.separator"));
        resultsText.append("ADCSnap3_Pwr = " + fmt.format(powerMPVs[3].getValue()));
        resultsText.append(System.getProperty("line.separator"));
        resultsText.append("ADCSnap4_Pwr = " + fmt.format(powerMPVs[4].getValue()));
        resultsText.append(System.getProperty("line.separator"));
        resultsText.append("=======================");
        resultsText.append(System.getProperty("line.separator"));
        storedEnergy = Un;
        fields[5] = getField(storedEnergy, 5);
        String strVal = fmt.format(fields[5]);
        fieldStrs[5] = strVal;
        fieldStrsHTML[5] = "<html><body><font color= green>" + strVal + "</font></body></html>";
        fieldsChanged = true;
        uc.update();
        asciiFileText = makeASCIIFileLine();
        analysisReady = true;
        return analysisReady;
    }
