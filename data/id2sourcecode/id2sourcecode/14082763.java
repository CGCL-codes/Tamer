    private void stringsMoveOneForward(String[] strings, int beginIndex) {
        for (int i = beginIndex; strings[i + 1] != null; i++) {
            strings[i] = strings[i + 1];
        }
    }
