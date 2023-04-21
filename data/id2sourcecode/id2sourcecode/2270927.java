    public static FixtureSource newJsonFile(final File jsonFile) throws FileNotFoundException {
        return new JSONSource(new RandomAccessFile(jsonFile, "r").getChannel());
    }
