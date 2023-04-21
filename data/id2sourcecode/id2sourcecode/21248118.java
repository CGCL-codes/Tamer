    private void backspace() {
        if (textIndex == 0) {
            return;
        }
        int nul = textIndex;
        while (text[nul] != 0 && nul < text.length) {
            ++nul;
        }
        for (int i = textIndex - 1; i < nul; ++i) {
            text[i] = text[i + 1];
        }
        text[nul] = 0;
        --textIndex;
    }
