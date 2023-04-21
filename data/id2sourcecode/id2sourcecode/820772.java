    public static void main(String[] args) {
        int nReaders = nicify(args[0], 1, 5);
        int nWriters = nicify(args[1], 1, 5);
        int nAccesses = Integer.parseInt(args[2]);
        String letters[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
        System.out.println("Starting RW with:\n" + nReaders + " readers,\n" + nWriters + " writers,\n" + nAccesses + " accesses.");
        Buffer buffer = new Buffer();
        for (int i = 0; i < nReaders; i++) {
            new Reader(letters[i], buffer, nAccesses).start();
        }
        for (int i = 0; i < nWriters; i++) {
            new Writer(letters[i + 5], buffer, nAccesses).start();
        }
    }
