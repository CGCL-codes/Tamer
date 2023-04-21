    private void _swapShort() {
        byte i = event[index];
        sevent[index] = event[index + 1];
        sevent[index + 1] = i;
        index += 2;
    }
