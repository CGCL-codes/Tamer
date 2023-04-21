    public void writeln(String s) {
        try {
            if (curRow < height - 1) {
                text[++curRow] = s;
            } else {
                for (int i = 0; i < height - 1; i++) {
                    text[i] = text[i + 1];
                }
                text[height - 1] = s;
            }
            conText.setValue(text);
            curCol = 0;
            cursor(curRow, -2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
