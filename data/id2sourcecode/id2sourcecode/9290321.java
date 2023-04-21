    private static Clip getClip(final AudioInputStream ais) throws LineUnavailableException, IOException {
        if (log.isTraceEnabled()) log.trace(HelperLog.methodStart(ais));
        try {
            final Info info = new DataLine.Info(Clip.class, ais.getFormat());
            final Clip result = (Clip) AudioSystem.getLine(info);
            result.open(ais);
            if (log.isTraceEnabled()) log.trace(HelperLog.methodExit(result));
            return result;
        } finally {
            ais.close();
        }
    }
