    private String getChannelLine(List<String> lines, int lineNumber) {
        int i = 0;
        for (String line : lines) {
            if (!line.startsWith(":->")) {
                i++;
                if (lineNumber == i) {
                    return line;
                }
            }
        }
        return null;
    }
