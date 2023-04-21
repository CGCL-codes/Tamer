    public long getIndexedLinePointer(POS pos, DictionaryFileType fileType, String target) throws IOException {
        RandomAccessDictionaryFile file = (RandomAccessDictionaryFile) getFile(pos, fileType);
        if (file == null || file.length() == 0) {
            return -1;
        }
        synchronized (file) {
            long start = 0;
            long stop = file.length();
            long offset = start, midpoint;
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
                        word = readLineWord(file);
                        if (word.equals(target)) {
                            return offset;
                        } else {
                            file.readLine();
                            offset = file.getFilePointer();
                        }
                    }
                    return -1;
                }
                word = readLineWord(file);
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
