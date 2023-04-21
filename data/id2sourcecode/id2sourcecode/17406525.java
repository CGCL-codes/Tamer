    public String toXhtml(String htmlText) {
        if (!isHtmlNode(htmlText)) {
            return null;
        }
        logger.fine("Enter toXhtml with " + htmlText);
        StringReader reader = new StringReader(htmlText);
        StringWriter writer = new StringWriter();
        try {
            XHTMLWriter.html2xhtml(reader, writer);
            String resultXml = writer.toString();
            if (!isWellformedXml(resultXml)) {
                return toXMLEscapedText(htmlText);
            }
            logger.fine("Leave toXhtml with " + resultXml);
            return resultXml;
        } catch (IOException e) {
            freemind.main.Resources.getInstance().logException(e);
        } catch (BadLocationException e) {
            freemind.main.Resources.getInstance().logException(e);
        }
        htmlText = htmlText.replaceAll("<", "&gt;");
        htmlText = htmlText.replaceAll(">", "&lt;");
        logger.fine("Leave toXhtml with fallback " + htmlText);
        return htmlText;
    }
