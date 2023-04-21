    @Override
    public void endMessage(VisitorContext ctx, Message message) {
        int expansion = charCount;
        if (wordCount <= threshold) {
            expansion = (expansion + 1) / 2;
        }
        StringBuilder expansionText = new StringBuilder();
        int wordIndex = 0;
        while (expansion > 0) {
            String word = NUMBERS[wordIndex++ % NUMBERS.length];
            expansionText.append(' ').append(word);
            expansion -= word.length() + 1;
        }
        NonlocalizableTextFragment suffix = ctx.createNonlocalizableTextFragment(expansionText.toString());
        ctx.insertAfter(null, suffix);
    }
