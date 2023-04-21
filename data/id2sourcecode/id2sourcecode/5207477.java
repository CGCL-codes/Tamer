    public Sequence getSequence(URL url) throws InvalidMidiDataException, IOException {
        InputStream inputStream = url.openStream();
        try {
            return getSequence(inputStream);
        } catch (InvalidMidiDataException e) {
            if (TDebug.TraceAllExceptions) {
                TDebug.out(e);
            }
            inputStream.close();
            throw e;
        } catch (IOException e) {
            if (TDebug.TraceAllExceptions) {
                TDebug.out(e);
            }
            inputStream.close();
            throw e;
        }
    }
