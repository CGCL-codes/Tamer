    public ParameterVO getParameterVO(String key) throws ParameterException {
        if (key == null) return null;
        String value = getParameter(key);
        if (value == null) return null;
        if (key.equals(PARAM_numBarcodesPerPrimerPair)) {
            return new DoubleParameterVO(1, 1000, 100);
        }
        if (key.equals(PARAM_barcodeLength)) {
            return new DoubleParameterVO(1, 100, 10);
        }
        if (key.equals(PARAM_maxFlows)) {
            return new DoubleParameterVO(1, 10, 5);
        }
        if (key.equals(PARAM_minEditDistance)) {
            return new DoubleParameterVO(1, 100, 3);
        }
        if (key.equals(PARAM_minPalindromeHBonds)) {
            return new DoubleParameterVO(1, 100, 14);
        }
        if (key.equals(PARAM_maxPalindromeMateDistance)) {
            return new DoubleParameterVO(1, 100, 11);
        }
        if (key.equals(PARAM_intDimerMaxScore)) {
            return new DoubleParameterVO(1, 100, 8);
        }
        if (key.equals(PARAM_endDimerMaxScore)) {
            return new DoubleParameterVO(1, 100, 3);
        }
        if (key.equals(PARAM_fivePrimeClamp) || key.equals(PARAM_flowSequence) || key.equals(PARAM_keyChar) || key.equals(PARAM_forwardPrimerAdapterSequence) || key.equals(PARAM_reversePrimerAdapterSequence)) {
            return new TextParameterVO(value);
        }
        if (key.equals(PARAM_primerFile) || key.equals(PARAM_ampliconsFile)) {
            return new MultiSelectVO(listOfStringsFromCsvString(value), listOfStringsFromCsvString(value));
        }
        if (key.equals(PARAM_attachBarcodeToForwardPrimer) || key.equals(PARAM_attachBarcodeToReversePrimer)) {
            return new BooleanParameterVO(Boolean.valueOf(value));
        }
        return null;
    }
