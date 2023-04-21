    private void initWriters(int writeThreadsCount, int writeTries) throws IOException {
        String name = getName() + "-writer-";
        Writer w;
        writers = new ArrayList<Writer>();
        for (int i = 0; i < writeThreadsCount; i++) {
            w = new Writer(name + (i + 1), writeTries, debugEnabled);
            writers.add(w);
            w.start();
        }
    }
