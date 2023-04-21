    private void onFocusLineChanged(int previousLine, int nextLine) {
        if (DEBUG) System.out.println("line: " + previousLine + " > " + nextLine);
        fFocusLine = nextLine;
        RevisionRange region = getRange(nextLine);
        updateFocusRange(region);
    }
