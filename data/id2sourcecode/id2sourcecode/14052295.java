    private void mode(IRCEvent event) {
        if (log.isLoggable(Level.FINE)) {
            log.fine(event.getRawEventData());
        }
        String[] rawTokens = event.getRawEventData().split("\\s+");
        String[] rawModeTokens = rawTokens[3].split("");
        String[] modeTokens = new String[rawModeTokens.length - 1];
        System.arraycopy(rawModeTokens, 1, modeTokens, 0, rawModeTokens.length - 1);
        String[] arguments = new String[rawTokens.length - 4];
        System.arraycopy(rawTokens, 4, arguments, 0, arguments.length);
        Map<String, List<String>> modeMap = new HashMap<String, List<String>>();
        ServerInformation info = event.getSession().getServerInformation();
        String[] channelPrefixes = info.getChannelPrefixes();
        boolean userMode = true;
        for (String prefix : channelPrefixes) {
            if (rawTokens[2].startsWith(prefix)) {
                userMode = false;
            }
        }
        if (userMode) {
            if (log.isLoggable(Level.INFO)) {
                log.info("MODE  " + Arrays.toString(modeTokens));
            }
            if (modeTokens[0].equals(":")) {
                String[] cleanTokens = new String[modeTokens.length - 1];
                System.arraycopy(modeTokens, 1, cleanTokens, 0, modeTokens.length - 1);
                modeTokens = cleanTokens;
            }
            char action = '+';
            List<String> targets = new ArrayList<String>();
            targets.add(event.getSession().getNick());
            for (String mode : modeTokens) {
                if (mode.equals("+") || mode.equals("-")) action = mode.charAt(0); else {
                    modeMap.put(action + mode, targets);
                }
            }
            ModeEvent me = new ModeEventImpl(event.getRawEventData(), event.getSession(), modeMap, rawTokens[0].substring(1).split("\\!")[0], null);
            manager.addToRelayList(me);
            return;
        }
        char action = '+';
        int argumntOffset = 0;
        for (String mode : modeTokens) {
            if (mode.equals("+") || mode.equals("-")) action = mode.charAt(0); else {
                ModeType type = info.getTypeForMode(mode);
                if (type == ModeType.GROUP_A || type == ModeType.GROUP_B) {
                    List<String> modeArgs = modeMap.get(action + mode);
                    if (modeArgs == null) modeArgs = new ArrayList<String>();
                    modeArgs.add(arguments[argumntOffset]);
                    System.err.println("Mode " + action + mode + " " + arguments[argumntOffset]);
                    argumntOffset++;
                    modeMap.put(action + mode, modeArgs);
                } else if (type == ModeType.GROUP_C) {
                    List<String> modeArgs = modeMap.get(action + mode);
                    if (modeArgs == null) modeArgs = new ArrayList<String>();
                    if (action == '-') {
                        if (!modeMap.containsKey(action + mode)) {
                            modeMap.put(action + mode, new ArrayList<String>());
                            System.err.println("Mode " + action + mode);
                        }
                    } else {
                        modeArgs.add(arguments[argumntOffset]);
                        System.err.println("Mode " + action + mode + " " + arguments[argumntOffset]);
                        argumntOffset++;
                        modeMap.put(action + mode, modeArgs);
                    }
                } else if (type == ModeType.GROUP_D) {
                    modeMap.put(action + mode, new ArrayList<String>());
                    System.err.println("Mode " + action + mode);
                } else {
                    System.err.println("unreconzied mode " + mode);
                }
            }
        }
        Channel chan = event.getSession().getChannel(rawTokens[2]);
        List<String> voicedNicks = modeMap.get("+v") == null ? new ArrayList<String>() : modeMap.get("+v");
        List<String> opedNicks = modeMap.get("+o") == null ? new ArrayList<String>() : modeMap.get("+o");
        List<String> devoicedNicks = modeMap.get("-v") == null ? new ArrayList<String>() : modeMap.get("-v");
        List<String> deopedNicks = modeMap.get("-o") == null ? new ArrayList<String>() : modeMap.get("-o");
        for (String nick : voicedNicks) {
            chan.updateUsersMode(nick, "+v");
        }
        for (String nick : opedNicks) {
            chan.updateUsersMode(nick, "+o");
        }
        for (String nick : devoicedNicks) {
            chan.updateUsersMode(nick, "-v");
        }
        for (String nick : deopedNicks) {
            chan.updateUsersMode(nick, "-o");
        }
        ModeEvent me = new ModeEventImpl(event.getRawEventData(), event.getSession(), modeMap, rawTokens[0].substring(1).split("\\!")[0], chan);
        manager.addToRelayList(me);
    }
