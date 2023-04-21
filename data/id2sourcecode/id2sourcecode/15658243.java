    public void run() throws IOException {
        FlatPackageWriterImpl flatPackageWriter = new FlatPackageWriterImpl();
        flatPackageWriter.setOutputDirectory(outputDir);
        Movie movie = new Movie();
        for (File input : inputFiles) {
            Movie m = MovieCreator.build(new FileInputStream(input).getChannel());
            for (Track track : m.getTracks()) {
                movie.addTrack(track);
            }
        }
        flatPackageWriter.write(movie);
    }
