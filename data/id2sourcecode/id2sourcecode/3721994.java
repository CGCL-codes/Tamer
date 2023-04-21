    private void backSpace() {
        int textPos = getTextPosition() - 1;
        if (textPos < 0) return;
        for (int i = textPos; i < text.length - 1; i++) text[i] = text[i + 1];
        text[text.length - 1] = fillValue;
        showText();
        setCaretPosition(textPos + textPos / 2, false, false);
    }
