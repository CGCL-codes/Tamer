    public void attributeDeclared(Element e, Attr a) {
        String name = a.getNodeName();
        if (checkedAtts.contains(name)) {
            if (!name.equals("readFormat") && !name.equals("writeFormat")) {
                if (!name.equals("dims")) {
                    if (!ReferenceAttribute.isValidIdRef(a, dom)) {
                        logger.warn("Error in Fsml2ndLevelValidation.");
                        eh.recordError(new InvalidIdRefException(e, a));
                    }
                } else {
                    if (!ReferenceAttributeList.isValidIdRef(a, dom)) {
                        logger.warn("Error in Fsml2ndLevelValidation.");
                        eh.recordError(new InvalidIdRefException(e, a));
                    }
                }
            } else {
                try {
                    FormatCache.getInstance().getFormat(a.getValue());
                } catch (Exception err) {
                    logger.warn("Error in Fsml2ndLevelValidation.");
                    eh.recordError(new InvalidFormatException(e, a));
                }
            }
        }
    }
