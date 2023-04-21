    public static String evalCmdDef(String[] words, int depth) {
        if ((words == null) || (words.length <= 1)) throw new RuntimeException("<def> needs at least 2 arguments");
        if (isBuiltinCommand(words[0])) throw new RuntimeException("Cannot define function : " + words[0]);
        String[] params = new String[words.length - 2];
        for (int i = 0; i < (words.length - 2); i++) params[i] = words[i + 1];
        ScriptFunction func = new ScriptFunction(words[0], params, words[words.length - 1], depth);
        ScriptParser.getFunctionsStore().put(func);
        return "1";
    }
