    FilesTests() throws IOException {
        String[] sets = { "US-ASCII", "ISO-8859-1", "UTF-8", "UTF-16" };
        for (int i = 0; i < sets.length; i++) {
            Log.log(sets[i] + " write/read");
            testCharacterSet(sets[i]);
        }
    }
