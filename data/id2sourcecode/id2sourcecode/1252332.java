    private void setPVText(BPM bpm1, BPM[] bpm2, BPM bpm3, HDipoleCorr hDipoleCorr, VDipoleCorr vDipoleCorr, Electromagnet quadMagnet) {
        StringBuffer scanPVX = new StringBuffer("Horizontal scan PV:\n");
        StringBuffer scanPVY = new StringBuffer("Vertical scan PV:\n");
        if (hDipoleCorr != null) {
            scanPVX.append("Corrector Scan PV: ");
            scanPVX.append(scanVariableCorrX.getChannelName());
            scanPVX.append("  ");
            scanPVX.append(connectionMap.get(HDipoleCorrPVName));
            scanPVX.append("\n");
            scanPVX.append("Quadrupole Scan PV: ");
            scanPVX.append(scanVariableQuadX.getChannelName());
            scanPVX.append("  ");
            scanPVX.append(connectionMap.get(QuadMagXSetPVName));
            scanPVX.append("\n");
        }
        if (vDipoleCorr != null) {
            scanPVY.append("Corrector SCAN PV: ");
            scanPVY.append(scanVariableCorrY.getChannelName());
            scanPVY.append("  ");
            scanPVY.append(connectionMap.get(VDipoleCorrPVName));
            scanPVY.append("\n");
            scanPVY.append("Quadrupole SCAN PV: ");
            scanPVY.append(scanVariableQuadY.getChannelName());
            scanPVY.append("  ");
            scanPVY.append(connectionMap.get(QuadMagYSetPVName));
            scanPVY.append("\n");
        }
        Iterator itrX = measuredXValuesV.iterator();
        int x = 1;
        String mvPVXs = "\n";
        while (itrX.hasNext()) {
            String name = ((MeasuredValue) itrX.next()).getChannelName();
            mvPVXs += "monitor PV " + (new Integer(x)).toString() + " : " + name + "  " + connectionMap.get(name) + "\n";
            x++;
        }
        Iterator itrY = measuredYValuesV.iterator();
        int y = 1;
        String mvPVYs = "\n";
        while (itrY.hasNext()) {
            String name = ((MeasuredValue) itrY.next()).getChannelName();
            mvPVYs += "monitor PV " + (new Integer(y)).toString() + " : " + name + "  " + connectionMap.get(name) + "\n";
            y++;
        }
        thePVTextX = scanPVX + mvPVXs;
        thePVTextY = scanPVY + mvPVYs;
    }
