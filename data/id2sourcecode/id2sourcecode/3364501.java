    public Element(String inValue, String inType, String inVocab, boolean writeableFlag, boolean readableFlag, boolean mandatoryFlag) {
        value = inValue;
        type = inType;
        vocabularyType = inVocab;
        writeable = writeableFlag;
        readable = readableFlag;
        if (inValue.equalsIgnoreCase("")) {
            initialized = false;
        } else {
            initialized = true;
        }
        mandatory = mandatoryFlag;
        implemented = true;
    }
