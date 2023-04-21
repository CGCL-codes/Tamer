    private void rb_set() {
        rb_setMap = new LinkedHashMap();
        ch_noiseMap = new LinkedHashMap();
        ch_offsetMap = new LinkedHashMap();
        Iterator iAllMag = mags.iterator();
        while (iAllMag.hasNext()) {
            Electromagnet em = (Electromagnet) iAllMag.next();
            rb_setMap.put(em.getChannel(Electromagnet.FIELD_RB_HANDLE), em.getMainSupply().getChannel(MagnetMainSupply.FIELD_SET_HANDLE));
            if (em.getType().equals("QH") || em.getType().equals("QV") || em.getType().equals("QTH") || em.getType().equals("QTV") || em.getType().equals("PMQH") || em.getType().equals("PMQV")) {
                ch_noiseMap.put(em.getChannel(Electromagnet.FIELD_RB_HANDLE), new Double(quadNoise));
                ch_offsetMap.put(em.getChannel(Electromagnet.FIELD_RB_HANDLE), new Double(quadOffset));
            } else if (em.getType().equals("DH")) {
                ch_noiseMap.put(em.getChannel(Electromagnet.FIELD_RB_HANDLE), new Double(dipoleNoise));
                ch_offsetMap.put(em.getChannel(Electromagnet.FIELD_RB_HANDLE), new Double(dipoleOffset));
            } else if (em.getType().equals("DCH") || em.getType().equals("DCV")) {
                ch_noiseMap.put(em.getChannel(Electromagnet.FIELD_RB_HANDLE), new Double(correctorNoise));
                ch_offsetMap.put(em.getChannel(Electromagnet.FIELD_RB_HANDLE), new Double(correctorOffset));
            }
        }
        Iterator iRfCavity = rfCavities.iterator();
        while (iRfCavity.hasNext()) {
            RfCavity rfCav = (RfCavity) iRfCavity.next();
            rb_setMap.put(rfCav.getChannel(RfCavity.CAV_AMP_AVG_HANDLE), rfCav.getChannel(RfCavity.CAV_AMP_SET_HANDLE));
            rb_setMap.put(rfCav.getChannel(RfCavity.CAV_PHASE_AVG_HANDLE), rfCav.getChannel(RfCavity.CAV_PHASE_SET_HANDLE));
            ch_noiseMap.put(rfCav.getChannel(RfCavity.CAV_AMP_AVG_HANDLE), new Double(rfAmpNoise));
            ch_noiseMap.put(rfCav.getChannel(RfCavity.CAV_PHASE_AVG_HANDLE), new Double(rfPhaseNoise));
            ch_offsetMap.put(rfCav.getChannel(RfCavity.CAV_AMP_AVG_HANDLE), new Double(rfAmpOffset));
            ch_offsetMap.put(rfCav.getChannel(RfCavity.CAV_PHASE_AVG_HANDLE), new Double(rfPhaseOffset));
        }
    }
