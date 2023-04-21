    private static Part elementToPart(Element element) throws ConversionException {
        XMLStyle xmlStyle = new StandardXMLStyle();
        if (!XMLStyles.isValidPartTag(element.getName())) {
            throw new ConversionException("Invalid element: " + element.getName() + ".  The only " + "accepted tag name is '" + xmlStyle.getPartTagName() + "'.");
        }
        Part returnPart = new Part();
        String attributeValue;
        attributeValue = XMLStyles.getTitleAttributeValue(element);
        if (!attributeValue.equals("")) {
            returnPart.setTitle(attributeValue);
        }
        attributeValue = XMLStyles.getChannelAttributeValue(element);
        if (!attributeValue.equals("")) {
            try {
                returnPart.setChannel(Integer.parseInt(attributeValue));
            } catch (NumberFormatException e) {
                throw new ConversionException("Invalid attribute value: " + attributeValue + ".  The " + "attribute '" + xmlStyle.getChannelAttributeName() + "' of element '" + xmlStyle.getPartTagName() + "' must represent a Java integer.");
            }
        }
        attributeValue = XMLStyles.getInstrumentAttributeValue(element);
        if (!attributeValue.equals("")) {
            try {
                returnPart.setInstrument(Integer.parseInt(attributeValue));
            } catch (NumberFormatException e) {
                throw new ConversionException("Invalid attribute value: " + attributeValue + ".  The " + "attribute '" + xmlStyle.getInstrumentAttributeName() + "' of element '" + xmlStyle.getPartTagName() + "' must represent a Java integer.");
            }
        }
        attributeValue = XMLStyles.getTempoAttributeValue(element);
        if (!attributeValue.equals("")) {
            try {
                returnPart.setTempo(Double.valueOf(attributeValue).doubleValue());
            } catch (NumberFormatException e) {
                throw new ConversionException("Invalid attribute value: " + attributeValue + ".  The " + "attribute '" + xmlStyle.getTempoAttributeName() + "' of element '" + xmlStyle.getPartTagName() + "' must represent a Java double.");
            }
        }
        attributeValue = XMLStyles.getVolumeAttributeValue(element);
        if (!attributeValue.equals("")) {
            try {
                returnPart.setVolume(Integer.parseInt(attributeValue));
            } catch (NumberFormatException e) {
                throw new ConversionException("Invalid attribute value: " + attributeValue + ".  The " + "attribute '" + xmlStyle.getVolumeAttributeName() + "' of element '" + xmlStyle.getPartTagName() + "' must represent a Java integer.");
            }
        }
        attributeValue = XMLStyles.getKeySignatureAttributeValue(element);
        if (!attributeValue.equals("")) {
            try {
                returnPart.setKeySignature(Integer.parseInt(attributeValue));
            } catch (NumberFormatException e) {
                throw new ConversionException("Invalid attribute value: " + attributeValue + ".  The " + "attribute '" + xmlStyle.getKeySignatureAttributeName() + "' of element '" + xmlStyle.getPartTagName() + "' must represent a Java integer.");
            }
        }
        attributeValue = XMLStyles.getKeyQualityAttributeValue(element);
        if (!attributeValue.equals("")) {
            try {
                returnPart.setKeyQuality(Integer.parseInt(attributeValue));
            } catch (NumberFormatException e) {
                throw new ConversionException("Invalid attribute value: " + attributeValue + ".  The " + "attribute '" + xmlStyle.getKeyQualityAttributeName() + "' of element '" + xmlStyle.getScoreTagName() + "' must represent a Java integer.");
            }
        }
        attributeValue = XMLStyles.getNumeratorAttributeValue(element);
        if (!attributeValue.equals("")) {
            try {
                returnPart.setNumerator(Integer.parseInt(attributeValue));
            } catch (NumberFormatException e) {
                throw new ConversionException("Invalid attribute value: " + attributeValue + ".  The " + "attribute '" + xmlStyle.getNumeratorAttributeName() + "' of element '" + xmlStyle.getPartTagName() + "' must represent a Java integer.");
            }
        }
        attributeValue = XMLStyles.getDenominatorAttributeValue(element);
        if (!attributeValue.equals("")) {
            try {
                returnPart.setDenominator(Integer.parseInt(attributeValue));
            } catch (NumberFormatException e) {
                throw new ConversionException("Invalid attribute value: " + attributeValue + ".  The " + "attribute '" + xmlStyle.getDenominatorAttributeName() + "' of element '" + xmlStyle.getPartTagName() + "' must represent a Java integer.");
            }
        }
        attributeValue = XMLStyles.getPanAttributeValue(element);
        if (!attributeValue.equals("")) {
            try {
                returnPart.setPan(Double.valueOf(attributeValue).doubleValue());
            } catch (NumberFormatException e) {
                throw new ConversionException("Invalid attribute value: " + attributeValue + ".  The " + "attribute '" + xmlStyle.getPanAttributeName() + "' of element '" + xmlStyle.getPartTagName() + "' must represent a Java double.");
            }
        }
        Element[] elements = element.getChildren();
        for (int i = 0; i < elements.length; i++) {
            if (XMLStyles.isValidPhraseTag(elements[i].getName())) {
                returnPart.addPhrase(elementToPhrase(elements[i]));
            }
        }
        return returnPart;
    }
