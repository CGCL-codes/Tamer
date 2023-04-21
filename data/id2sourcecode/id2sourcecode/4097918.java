    void addLine(String s) {
        if (curRow < text.length - 1) {
            text[++curRow] = s;
        } else {
            for (int i = 0; i < text.length - 1; i++) {
                text[i] = text[i + 1];
            }
            text[text.length - 1] = s;
        }
    }
