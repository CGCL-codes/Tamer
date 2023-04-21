    public void readCommand(OsProcess proc) {
        String input = null;
        try {
            while ((input = in.readLine()) == null) ;
        } catch (IOException e) {
            System.err.println("readCommand failed.");
            System.exit(1);
        }
        String[] words = toArgs(input);
        if (words != null) {
            if (words.length != 0) {
                cmd = words[0];
                args = new String[words.length - 1];
                for (int i = 0; i < args.length; ++i) args[i] = words[i + 1];
            }
        }
    }
