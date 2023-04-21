    public void AppendHistoryLine(String line) {
        if (currentHistoryLine == nrHistoryLines) {
            int i;
            for (i = 0; i < currentHistoryLine - 1; i++) {
                history[i] = history[i + 1];
            }
            currentHistoryLine--;
        }
        history[currentHistoryLine] = line;
        currentHistoryLine++;
    }
