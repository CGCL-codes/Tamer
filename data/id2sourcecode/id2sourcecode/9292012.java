    public static void main(String[] args) throws Exception {
        Consumer<String> pipeline = Builder.SCB.retrieveUrls().readRssItems().selectIfField("description", containsString("to")).formatAsCsv().writeToFile(NEWS_FILE_NAME).build();
        System.exit(new Runner<String>(pipeline).run("http://localhost:8123/"));
    }
