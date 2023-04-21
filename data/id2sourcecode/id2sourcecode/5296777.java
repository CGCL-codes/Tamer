    public static void updateFileHistory(File latestFilename) {
        read();
        boolean alreadyExists = false;
        for (int h = historySize - 1; h >= 0; h--) {
            if (latestFilename.getPath().equals(filename[h].getPath())) {
                alreadyExists = true;
                File file = filename[h];
                for (int y = h; y < historySize - 1; y++) filename[y] = filename[y + 1];
                filename[historySize - 1] = file;
                write();
            }
        }
        if (!alreadyExists) {
            if (historySize < maxHistorySize) {
                filename[historySize] = latestFilename;
                historySize++;
            } else {
                for (int i = 0; i < historySize - 1; i++) filename[i] = filename[i + 1];
                filename[historySize - 1] = latestFilename;
            }
            write();
        }
    }
