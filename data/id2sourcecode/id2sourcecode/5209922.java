    static String rsToChaiXML(final ChaiResponseSet rs) throws ChaiValidationException {
        final Element rootElement = new Element(XML_NODE_ROOT);
        rootElement.setAttribute(XML_ATTRIBUTE_MIN_RANDOM_REQUIRED, String.valueOf(rs.getChallengeSet().getMinRandomRequired()));
        rootElement.setAttribute(XML_ATTRIBUTE_LOCALE, rs.getChallengeSet().getLocale().toString());
        rootElement.setAttribute(XML_ATTRIBUTE_VERSION, VALUE_VERSION);
        rootElement.setAttribute(XML_ATTRIBUTE_CHAI_VERSION, ChaiConstant.CHAI_API_VERSION);
        if (rs.caseInsensitive) {
            rootElement.setAttribute(XML_ATTRIBUTE_CASE_INSENSITIVE, "true");
        }
        if (rs.csIdentifier != null) {
            rootElement.setAttribute(XML_ATTRIBUTE_CHALLENGE_SET_IDENTIFER, rs.csIdentifier);
        }
        if (rs.timestamp != null) {
            rootElement.setAttribute(XML_ATTRIBUTE_TIMESTAMP, DATE_FORMATTER.format(rs.timestamp));
        }
        for (final Challenge loopChallenge : rs.crMap.keySet()) {
            String loopResponseText = rs.caseInsensitive ? rs.crMap.get(loopChallenge).toLowerCase() : rs.crMap.get(loopChallenge);
            final Element loopElement = new Element(XML_NODE_RESPONSE);
            loopElement.addContent(new Element(XML_NODE_CHALLENGE).addContent(new CDATA(loopChallenge.getChallengeText())));
            {
                final Element contentElement = new Element(XML_NODE_ANSWER_VALUE);
                contentElement.setAttribute(XNL_ATTRIBUTE_CONTENT_FORMAT, rs.formatType.toString());
                switch(rs.formatType) {
                    case TEXT:
                        contentElement.addContent(new CDATA(loopResponseText));
                        break;
                    case SHA1_SALT:
                        final String salt = generateSalt(32);
                        loopResponseText = salt + loopResponseText;
                        contentElement.setAttribute(XML_ATTRIBUTE_SALT, salt);
                    case SHA1:
                        try {
                            final MessageDigest md = MessageDigest.getInstance("SHA1");
                            final byte[] hashedAnswer = md.digest((loopResponseText).getBytes());
                            final String encodedAnswer = Base64Util.encodeBytes(hashedAnswer);
                            contentElement.addContent(new CDATA(encodedAnswer));
                        } catch (NoSuchAlgorithmException e) {
                            LOGGER.warn("error while hashing Chai SHA1 response: " + e.getMessage());
                        }
                }
                loopElement.addContent(contentElement);
            }
            loopElement.setAttribute(XML_ATTRIBUTE_ADMIN_DEFINED, String.valueOf(loopChallenge.isAdminDefined()));
            loopElement.setAttribute(XML_ATTRIBUTE_REQUIRED, String.valueOf(loopChallenge.isRequired()));
            loopElement.setAttribute(XNL_ATTRIBUTE_MIN_LENGTH, String.valueOf(loopChallenge.getMinLength()));
            loopElement.setAttribute(XNL_ATTRIBUTE_MAX_LENGTH, String.valueOf(loopChallenge.getMaxLength()));
            rootElement.addContent(loopElement);
        }
        final Document doc = new Document(rootElement);
        final XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(Format.getCompactFormat().setTextMode(Format.TextMode.NORMALIZE));
        return outputter.outputString(doc);
    }
