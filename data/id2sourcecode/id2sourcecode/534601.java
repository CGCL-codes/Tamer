    public void readCommand(OsProcess proc) {
        String str;
        user = proc;
        if (rawTTY) str = readFromRawTTY(); else str = readFromCookedTTY();
        String[] words = toArgs(str);
        if (words != null) {
            if (words.length != 0) {
                cmd = words[0];
                args = new String[words.length - 1];
                for (int i = 0; i < args.length; ++i) args[i] = words[i + 1];
            }
        }
    }
