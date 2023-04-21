    @Override
    protected void doPrintLabelledMessage(final String label, final String message) {
        if (label != null && label.length() > longestLabelLength) {
            longestLabelLength = label.length();
        }
        final String printedLabel = StringUtils.leftPad(label != null ? label : StringUtils.EMPTY, longestLabelLength, ' ');
        final String rawPrintedMessage = WordUtils.wrap(message, SCREEN_WIDTH - SEPARATOR.length() - longestLabelLength, "|", true);
        final String[] printedMessageLines = rawPrintedMessage.split("\\|");
        final StringBuffer firstLine = new StringBuffer(printedLabel).append(SEPARATOR).append(printedMessageLines[0]);
        out.println(firstLine);
        if (printedMessageLines.length > 0) {
            final String leftPadding = StringUtils.leftPad(StringUtils.EMPTY, SEPARATOR.length() + longestLabelLength, ' ');
            for (int i = 1; i < printedMessageLines.length; i++) {
                final StringBuffer nextLine = new StringBuffer(leftPadding).append(printedMessageLines[i]);
                out.println(nextLine);
            }
        }
    }
