    private String auxFuncions(FiniteAutomata fa, Options options) {
        String nextState;
        switch(options.scannerTable) {
            case FULL:
                nextState = "    private int nextState(char c, int state)\n" + "    {\n" + "        int next = SCANNER_TABLE[state][c];\n" + "        return next;\n" + "    }\n";
                break;
            case COMPACT:
                nextState = "    private int nextState(char c, int state)\n" + "    {\n" + "        int start = SCANNER_TABLE_INDEXES[state];\n" + "        int end   = SCANNER_TABLE_INDEXES[state+1]-1;\n" + "\n" + "        while (start <= end)\n" + "        {\n" + "            int half = (start+end)/2;\n" + "\n" + "            if (SCANNER_TABLE[half][0] == c)\n" + "                return SCANNER_TABLE[half][1];\n" + "            else if (SCANNER_TABLE[half][0] < c)\n" + "                start = half+1;\n" + "            else  //(SCANNER_TABLE[half][0] > c)\n" + "                end = half-1;\n" + "        }\n" + "\n" + "        return -1;\n" + "    }\n";
                break;
            case HARDCODE:
                {
                    List<Map<Character, Integer>> trans = fa.getTransitions();
                    StringBuffer casesState = new StringBuffer();
                    for (int i = 0; i < trans.size(); i++) {
                        Map<Character, Integer> m = trans.get(i);
                        if (m.size() == 0) continue;
                        casesState.append("            case " + i + ":\n" + "                switch (c)\n" + "                {\n");
                        for (Map.Entry<Character, Integer> entry : m.entrySet()) {
                            Character ch = entry.getKey();
                            Integer it = entry.getValue();
                            casesState.append("                    case " + ((int) ch.charValue()) + ": return " + it + ";\n");
                        }
                        casesState.append("                    default: return -1;\n" + "                }\n");
                    }
                    nextState = "    private int nextState(char c, int state)\n" + "    {\n" + "        switch (state)\n" + "        {\n" + casesState.toString() + "            default: return -1;\n" + "        }\n" + "    }\n";
                }
                break;
            default:
                nextState = null;
        }
        return nextState + "\n" + "    private int tokenForState(int state)\n" + "    {\n" + "        if (state < 0 || state >= TOKEN_STATE.length)\n" + "            return -1;\n" + "\n" + "        return TOKEN_STATE[state];\n" + "    }\n" + "\n" + (lookup ? "    public int lookupToken(int base, String key)\n" + "    {\n" + "        int start = SPECIAL_CASES_INDEXES[base];\n" + "        int end   = SPECIAL_CASES_INDEXES[base+1]-1;\n" + "\n" + (sensitive ? "" : "        key = key.toUpperCase();\n" + "\n") + "        while (start <= end)\n" + "        {\n" + "            int half = (start+end)/2;\n" + "            int comp = SPECIAL_CASES_KEYS[half].compareTo(key);\n" + "\n" + "            if (comp == 0)\n" + "                return SPECIAL_CASES_VALUES[half];\n" + "            else if (comp < 0)\n" + "                start = half+1;\n" + "            else  //(comp > 0)\n" + "                end = half-1;\n" + "        }\n" + "\n" + "        return base;\n" + "    }\n" + "\n" : "") + "    private boolean hasInput()\n" + "    {\n" + "        return position < input.length();\n" + "    }\n" + "\n" + "    private char nextChar()\n" + "    {\n" + "        if (hasInput())\n" + "            return input.charAt(position++);\n" + "        else\n" + "            return (char) -1;\n" + "    }\n" + "";
    }
