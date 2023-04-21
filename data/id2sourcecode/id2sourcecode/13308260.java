    public IRCEvent createEvent(IRCEvent event) {
        boolean userMode = event.numeric() != 324 && !event.getSession().isChannelToken(event.arg(0));
        char[] modeTokens = new char[0];
        String[] arguments = new String[0];
        int modeOffs = event.numeric() == 324 ? 2 : 1;
        modeTokens = event.arg(modeOffs).toCharArray();
        int size = event.args().size();
        if (modeOffs + 1 < size) {
            arguments = event.args().subList(modeOffs + 1, event.args().size()).toArray(arguments);
        }
        int argumntOffset = 0;
        char action = '+';
        List<ModeAdjustment> modeAdjustments = new ArrayList<ModeAdjustment>();
        for (char mode : modeTokens) {
            if (mode == '+' || mode == '-') action = mode; else {
                if (userMode) {
                    String argument = argumntOffset >= arguments.length ? "" : arguments[argumntOffset];
                    modeAdjustments.add(new ModeAdjustment(action == '+' ? Action.PLUS : Action.MINUS, mode, argument));
                    argumntOffset++;
                } else {
                    ServerInformation info = event.getSession().getServerInformation();
                    ModeType type = info.getTypeForMode(String.valueOf(mode));
                    if (type == ModeType.GROUP_A || type == ModeType.GROUP_B) {
                        modeAdjustments.add(new ModeAdjustment(action == '+' ? Action.PLUS : Action.MINUS, mode, arguments[argumntOffset]));
                        argumntOffset++;
                    } else if (type == ModeType.GROUP_C) {
                        if (action == '-') {
                            modeAdjustments.add(new ModeAdjustment(Action.MINUS, mode, ""));
                        } else {
                            modeAdjustments.add(new ModeAdjustment(Action.PLUS, mode, arguments[argumntOffset]));
                            argumntOffset++;
                        }
                    } else if (type == ModeType.GROUP_D) {
                        modeAdjustments.add(new ModeAdjustment(action == '+' ? Action.PLUS : Action.MINUS, mode, ""));
                    } else {
                        System.err.println("unreconzied mode " + mode);
                    }
                }
            }
        }
        if (userMode) {
            return new ModeEvent(ModeEvent.ModeType.USER, event.getRawEventData(), event.getSession(), modeAdjustments, event.getSession().getConnectedHostName(), null);
        }
        return new ModeEvent(ModeEvent.ModeType.CHANNEL, event.getRawEventData(), event.getSession(), modeAdjustments, event.numeric() == 324 ? "" : event.getNick(), event.getSession().getChannel(event.numeric() == 324 ? event.arg(1) : event.arg(0)));
    }
