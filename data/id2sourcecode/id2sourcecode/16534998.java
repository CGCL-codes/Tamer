    public void save() throws IOException {
        new SpreadsheetStoryWriter(new FileOutputStream(path)).writeStories(stories);
    }
