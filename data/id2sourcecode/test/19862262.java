    public void reportIOException(IOException e, File f) {
        reportException("Error: Could not access/write to file.", "I could not access/write to the file '" + f.getAbsolutePath() + "'! Are you sure you have permissions to read from or write to this file?\n\nThe technical description of the error I got is: " + e.getMessage());
    }
