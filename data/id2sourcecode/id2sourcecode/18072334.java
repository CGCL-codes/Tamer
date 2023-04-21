    private void setDefaultValues() {
        setParameter(PARAM_primerFile, "");
        setParameter(PARAM_ampliconsFile, "");
        setParameter(PARAM_numBarcodesPerPrimerPair, "100");
        setParameter(PARAM_barcodeLength, "10");
        setParameter(PARAM_fivePrimeClamp, "CG");
        setParameter(PARAM_maxFlows, "5");
        setParameter(PARAM_flowSequence, "TACG");
        setParameter(PARAM_keyChar, "TCAG");
        setParameter(PARAM_minEditDistance, "3");
        setParameter(PARAM_minPalindromeHBonds, "14");
        setParameter(PARAM_maxPalindromeMateDistance, "11");
        setParameter(PARAM_intDimerMaxScore, "8");
        setParameter(PARAM_endDimerMaxScore, "3");
        setParameter(PARAM_forwardPrimerAdapterSequence, "");
        setParameter(PARAM_reversePrimerAdapterSequence, "");
        setParameter(PARAM_attachBarcodeToForwardPrimer, "false");
        setParameter(PARAM_attachBarcodeToReversePrimer, "false");
        this.taskName = TASK_NAME;
    }
