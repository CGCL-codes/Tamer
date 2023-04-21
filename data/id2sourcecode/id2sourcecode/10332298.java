    public void seekExactFrac(OggStreamHandler<?> primary, double frac) throws IOException {
        if (!isSeekable()) {
            throw new IOException("Current OggInput isn't seekable");
        }
        double min = 0.0;
        double max = 1.0;
        double time = (primary.getEndTime() > 0 ? frac * primary.getEndTime() : 0);
        double guess = frac;
        double lastGuess = 0;
        double lastError = Float.MAX_VALUE;
        do {
            seekApprox(guess);
            while (!isEOF() && !primary.trySync()) {
                read();
            }
            double curTime = Math.max(0, primary.getTime());
            double error = Math.abs(curTime - time);
            if (error < lastError) {
                if (curTime > time) {
                    max = guess;
                } else {
                    min = guess;
                }
                lastError = error;
                lastGuess = guess;
                guess = min + (max - min) / 2;
            } else {
                guess = lastGuess;
                break;
            }
        } while (true);
        seekApprox(guess);
        while (!isEOF() && !primary.trySync()) {
            read();
        }
        RandomOggInput rinput = (RandomOggInput) input;
        if (!rinput.isReadSlow()) {
            while (!isEOF() && !primary.trySkipTo(time)) {
                read();
            }
        }
    }
