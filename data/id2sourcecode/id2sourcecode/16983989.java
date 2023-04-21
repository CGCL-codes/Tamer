    private long fileGetIndexedLinePointer(RandomAccessDictionaryFile file, String target) throws IOException {
        if (file == null || file.length() == 0) {
            return -1;
        }
        synchronized (file) {
            long start = 0;
            long stop = file.length();
            long offset, midpoint;
            int compare;
            String word;
            while (true) {
                midpoint = (start + stop) / 2;
                file.seek(midpoint);
                file.readLine();
                offset = file.getFilePointer();
                if (stop == offset) {
                    file.seek(start);
                    offset = file.getFilePointer();
                    while (offset != stop) {
                        word = file.readLineWord();
                        if (word.equals(target)) {
                            return offset;
                        } else {
                            file.readLine();
                            offset = file.getFilePointer();
                        }
                    }
                    return -1;
                }
                word = file.readLineWord();
                compare = word.compareTo(target);
                if (compare == 0) {
                    return offset;
                } else if (compare > 0) {
                    stop = offset;
                } else {
                    start = offset;
                }
            }
        }
    }
