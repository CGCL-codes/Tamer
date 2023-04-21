    private void outputModelBreak(String prefix, String replacement, String suffix) {
        for (ModelBreaker breaker = new ModelBreaker(prefix, replacement, suffix, lineLength); breaker.hasNextLine(); ) {
            write(breaker.nextLine());
            outputNewline();
        }
    }
