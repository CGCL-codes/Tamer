    private static void doRetrieve(File fromFile, double rowsColumns[][]) throws IOException {
        if (!fromFile.canRead()) return;
        FileInputStream f = new FileInputStream(fromFile);
        try {
            FileChannel ch = f.getChannel();
            long offset = 0;
            for (double[] row : rowsColumns) {
                Mmap.doubles(row, 0, offset, row.length, ch);
                offset += row.length * 8;
            }
        } finally {
            f.close();
        }
    }
