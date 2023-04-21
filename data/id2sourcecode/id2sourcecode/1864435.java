    private static boolean readBytesUpTo(InputStream input, OutputStream output, byte[] pattern) throws IOException {
        if ((pattern == null) || (pattern.length == 0)) {
            throw new IllegalArgumentException("Specified pattern is null or empty.");
        }
        int patternIndex = 0;
        boolean matched = false;
        boolean atEnd = false;
        while ((!matched) && (!atEnd)) {
            int readByte = input.read();
            if (readByte < 0) {
                atEnd = true;
                if (patternIndex != 0) {
                    output.write(pattern, 0, patternIndex);
                    patternIndex = 0;
                }
            } else {
                if (readByte == pattern[patternIndex]) {
                    patternIndex++;
                    if (patternIndex >= pattern.length) {
                        matched = true;
                    }
                } else {
                    if (patternIndex != 0) {
                        output.write(pattern, 0, patternIndex);
                        patternIndex = 0;
                    }
                    output.write(readByte);
                }
            }
        }
        return matched;
    }
