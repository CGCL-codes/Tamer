    private static String extractText(final char[] chars, final int firstChar, int charCount) {
        final int maxIndex = Math.min(chars.length, firstChar + charCount);
        boolean anyChanges = true;
        while (anyChanges) {
            anyChanges = false;
            for (int i = firstChar; i < maxIndex - 1; i++) {
                if (chars[i] == ' ' && chars[i + 1] == ' ') {
                    for (int j = i + 1; j < maxIndex - 1; j++) {
                        chars[j] = chars[j + 1];
                    }
                    charCount--;
                    anyChanges = true;
                }
            }
        }
        final String returnValue = String.copyValueOf(chars, firstChar, charCount);
        return returnValue.trim();
    }
