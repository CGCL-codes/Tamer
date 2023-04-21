    public static void write(URL url, MeasureOfEffectivenessAnalysis analysis) throws IOException {
        new ObjectOutputStream(url.openConnection().getOutputStream()).writeObject(analysis);
    }
