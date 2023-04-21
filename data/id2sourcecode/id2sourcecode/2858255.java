    public boolean addChar(char c) {
        int i;
        for (i = 0; i < match.length - 1; i++) match[i] = match[i + 1];
        match[match.length - 1] = c;
        for (i = 0; i < match.length; i++) if (match[i] != pattern[i]) return false;
        return true;
    }
