    public ChatLine(LogLine logLine) {
        avatar = logLine.getAvatarName();
        server = logLine.getAvatarServer();
        time = logLine.timeStr;
        String str = logLine.content;
        if (str.startsWith(YOU_AS_SENDER + " ")) {
            sender = YOU_AS_SENDER;
            str = str.substring(YOU_AS_SENDER.length() + 1);
        } else if (str.startsWith(ChatLink.PREFIX)) {
            sender = ChatLink.parseTitle(str);
            str = str.substring(str.indexOf(ChatLink.POSTFIX) + ChatLink.POSTFIX.length() + 1);
        }
        verb = str.substring(0, str.indexOf(','));
        if (PATTERN_SAYS_TO_GROUP.matcher(str).matches()) {
            type = TYPE_GROUP;
            reciever = GROUP;
            verb = "sagt zur";
            str = str.substring(str.indexOf(' ') + 1);
        } else if (PATTERN_SAYS_TO_GUILD.matcher(str).matches()) {
            type = TYPE_GUILD;
            reciever = GUILD;
            verb = "sagt zur";
            str = str.substring(str.indexOf(' ') + 1);
        } else if (PATTERN_SAYS_TO_RAID.matcher(str).matches()) {
            type = TYPE_RAID;
            reciever = RAID;
            verb = "sagt zum";
            str = str.substring(str.indexOf(' ') + 1);
        } else if (PATTERN_SHOUTS.matcher(str).matches()) {
            type = TYPE_SHOUT;
            reciever = SHOUT;
            str = str.substring(str.indexOf(' ') + 1);
        } else if (PATTERN_SAYS_OOC.matcher(str).matches()) {
            type = TYPE_OOC;
            reciever = OOC;
            verb = "sagt als Spielerkommentar";
            str = str.substring(str.indexOf(' ') + 1);
        } else if (PATTERN_SAYS_OFFICER.matcher(str).matches()) {
            type = TYPE_OFFICER;
            reciever = OFFICER;
            verb = "sagt zu";
            str = str.substring(str.indexOf(' ') + 1);
        } else if (PATTERN_NPC_SAYS.matcher(str).matches()) {
            type = TYPE_NPC_SAY;
            int index = str.indexOf(REGEX_TO) + REGEX_TO.length() - 1;
            verb = str.substring(0, index);
            str = str.substring(index + 1);
        } else if (PATTERN_SAYS.matcher(str).matches()) {
            type = TYPE_SAY;
            reciever = SAY;
            str = str.substring(str.indexOf(',') + 2);
        } else if (PATTERN_TELLS_TO.matcher(str).matches()) {
            type = TYPE_TELL;
            int endIndex = str.indexOf(REGEX_TO) + REGEX_TO.length() - 1;
            verb = "sagt zu";
            str = str.substring(endIndex + 1);
        } else if (PATTERN_TELLS.matcher(str).matches()) {
            type = TYPE_TELL;
            verb = "sagt zu";
            str = str.substring(str.indexOf(' ') + 1);
        } else {
            System.out.println("UNKNOWN: " + logLine);
            type = TYPE_UNKNOWN;
            reciever = UNKNOWN;
        }
        if (type == TYPE_TELL || type == TYPE_NPC_SAY) {
            reciever = str.substring(0, str.indexOf(','));
            Matcher channelNumberMatcher = PATTERN_CHANNEL_NUMBER.matcher(reciever);
            if (channelNumberMatcher.find()) {
                isTargetChannel = true;
                channelNumber = reciever.substring(channelNumberMatcher.start(), channelNumberMatcher.end());
                reciever = reciever.substring(0, channelNumberMatcher.start() - 1);
                type = TYPE_TELL;
            } else if (type == TYPE_TELL) {
                reciever = reciever.substring(0, 1).toUpperCase() + reciever.substring(1).toLowerCase();
            }
            int i = reciever.indexOf('.') + 1;
            if (i > 0 && type == TYPE_TELL) {
                String newReciever = reciever.substring(0, i);
                newReciever += reciever.substring(i, i + 1).toUpperCase();
                newReciever += reciever.length() > i ? reciever.substring(i + 1).toLowerCase() : "";
                reciever = newReciever;
            }
            str = str.substring(str.indexOf(',') + 1);
            if (reciever.equalsIgnoreCase(YOU_AS_RECIEVER)) {
                reciever = YOU_AS_RECIEVER;
            }
        }
        msg = str.substring(str.indexOf('"') + 1, str.lastIndexOf('"'));
    }
