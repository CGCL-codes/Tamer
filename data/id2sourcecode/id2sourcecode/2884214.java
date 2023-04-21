    public static String generateString(int minLength, int maxLength) {
        StringBuilder sb = new StringBuilder();
        int length = minLength + random.nextInt(maxLength - minLength);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS[random.nextInt(CHARACTERS.length)]);
        }
        return sb.toString();
    }
