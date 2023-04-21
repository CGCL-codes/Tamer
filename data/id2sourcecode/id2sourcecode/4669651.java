    private Object[] fixContinuedLines(String[] code) {
        int arrayLen = code.length;
        int[] reallines = new int[code.length];
        for (int i = 0; i < reallines.length; i++) reallines[i] = i;
        for (int i = 0; i < arrayLen; i++) {
            if (code[i].endsWith("..")) {
                if (code.length - 1 > i + 1) {
                    code[i] = code[i].substring(0, code[i].length() - 2) + code[i + 1];
                    arrayLen--;
                    for (int l = i + 1; l < arrayLen; l++) {
                        code[l] = code[l + 1];
                        reallines[l]++;
                    }
                    i--;
                }
            }
        }
        return new Object[] { Arrays.copyOf(code, arrayLen), reallines };
    }
