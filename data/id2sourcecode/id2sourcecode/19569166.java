    public Object newValue(Random random) {
        if (random.nextBoolean()) {
            return null;
        }
        StringBuilder strBuilder = new StringBuilder();
        int remainingLength = minLength + random.nextInt(maxLength - minLength);
        boolean endsWithPunctuation = random.nextBoolean();
        if (endsWithPunctuation) {
            remainingLength--;
        }
        String word = getWord(remainingLength, random);
        strBuilder.append(word);
        remainingLength -= word.length();
        while (remainingLength > 1) {
            strBuilder.append(getChar(midPunctuation, random));
            remainingLength--;
            word = getWord(remainingLength, random);
            strBuilder.append(word);
            remainingLength -= word.length();
        }
        if (endsWithPunctuation) {
            strBuilder.append(getChar(endPunctuation, random));
        }
        return strBuilder.toString();
    }
