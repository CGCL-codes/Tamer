    private static String partToXMLString(final Part part, final XMLStyle xmlStyle) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(xmlStyle.getLeftAngleBracket() + xmlStyle.getPartTagName());
        if (!part.getTitle().equals(Part.DEFAULT_TITLE)) {
            buffer.append(xmlStyle.getSpace() + xmlStyle.getTitleAttributeName() + "=" + xmlStyle.getDoubleQuote());
            String title = part.getTitle();
            for (int i = 0; i < title.length(); i++) {
                char character = title.charAt(i);
                if (character == ' ') {
                    buffer.append(xmlStyle.getSpace());
                } else if (character == '/') {
                    buffer.append(xmlStyle.getSlash());
                } else if (character == '&') {
                    buffer.append(xmlStyle.getAmpersandInString());
                } else if (character == '<') {
                    buffer.append(xmlStyle.getLeftAngleBracketInString());
                } else if (character == '>') {
                    buffer.append(xmlStyle.getRightAngleBracketInString());
                } else if (character == '"') {
                    buffer.append(xmlStyle.getDoubleQuoteInString());
                } else if (character == '#') {
                    buffer.append(xmlStyle.getHash());
                } else if (character == '?') {
                    buffer.append(xmlStyle.getQuestionMark());
                } else if (character == ';') {
                    buffer.append(xmlStyle.getSemicolon());
                } else {
                    buffer.append(character);
                }
            }
            buffer.append(xmlStyle.getDoubleQuote());
        }
        if (part.getChannel() != Part.DEFAULT_CHANNEL) {
            buffer.append(xmlStyle.getSpace() + xmlStyle.getChannelAttributeName() + "=" + xmlStyle.getDoubleQuote() + Integer.toString(part.getChannel()) + xmlStyle.getDoubleQuote());
        }
        if (part.getInstrument() != Part.DEFAULT_INSTRUMENT) {
            buffer.append(xmlStyle.getSpace() + xmlStyle.getInstrumentAttributeName() + "=" + xmlStyle.getDoubleQuote() + Integer.toString(part.getInstrument()) + xmlStyle.getDoubleQuote());
        }
        if (part.getTempo() != Part.DEFAULT_TEMPO) {
            buffer.append(xmlStyle.getSpace() + xmlStyle.getTempoAttributeName() + "=" + xmlStyle.getDoubleQuote() + (xmlStyle.limitDecimalPlaces() ? XMLParser.limitDecimalPlaces(part.getTempo(), 2) : Double.toString(part.getTempo())) + xmlStyle.getDoubleQuote());
        }
        if (part.getVolume() != Part.DEFAULT_VOLUME) {
            buffer.append(xmlStyle.getSpace() + xmlStyle.getVolumeAttributeName() + "=" + xmlStyle.getDoubleQuote() + Integer.toString(part.getVolume()) + xmlStyle.getDoubleQuote());
        }
        if (part.getKeySignature() != Part.DEFAULT_KEY_SIGNATURE) {
            buffer.append(xmlStyle.getSpace() + xmlStyle.getKeySignatureAttributeName() + "=" + xmlStyle.getDoubleQuote() + Integer.toString(part.getKeySignature()) + xmlStyle.getDoubleQuote());
        }
        if (part.getKeyQuality() != Part.DEFAULT_KEY_QUALITY) {
            buffer.append(xmlStyle.getSpace() + xmlStyle.getKeyQualityAttributeName() + "=" + xmlStyle.getDoubleQuote() + Integer.toString(part.getKeyQuality()) + xmlStyle.getDoubleQuote());
        }
        if (part.getNumerator() != Part.DEFAULT_NUMERATOR) {
            buffer.append(xmlStyle.getSpace() + xmlStyle.getNumeratorAttributeName() + "=" + xmlStyle.getDoubleQuote() + Integer.toString(part.getNumerator()) + xmlStyle.getDoubleQuote());
        }
        if (part.getDenominator() != Part.DEFAULT_DENOMINATOR) {
            buffer.append(xmlStyle.getSpace() + xmlStyle.getDenominatorAttributeName() + "=" + xmlStyle.getDoubleQuote() + Integer.toString(part.getDenominator()) + xmlStyle.getDoubleQuote());
        }
        if (part.getPan() != Part.DEFAULT_PAN) {
            buffer.append(xmlStyle.getSpace() + xmlStyle.getPanAttributeName() + "=" + xmlStyle.getDoubleQuote() + (xmlStyle.limitDecimalPlaces() ? XMLParser.limitDecimalPlaces(part.getPan(), 2) : Double.toString(part.getPan())) + xmlStyle.getDoubleQuote());
        }
        int size = part.size();
        if (size == 0) {
            buffer.append(xmlStyle.getSlash() + xmlStyle.getRightAngleBracket());
        } else {
            buffer.append(xmlStyle.getRightAngleBracket());
            for (int i = 0; i < part.size(); i++) {
                buffer.append(phraseToXMLString(part.getPhrase(i), xmlStyle));
            }
            buffer.append(xmlStyle.getLeftAngleBracket() + xmlStyle.getSlash() + xmlStyle.getPartTagName() + xmlStyle.getRightAngleBracket());
        }
        return buffer.toString();
    }
