    public void prepare(AudioAnalyzerResults audioAnalyzerResults) throws IOException {
        this.waveformLevelsContainer.prepare(audioAnalyzerResults.getFormat().getChannels(), audioAnalyzerResults.getFormat().getEncoding().toString());
        this.waveformCanvasesContainer.prepare(audioAnalyzerResults);
    }
