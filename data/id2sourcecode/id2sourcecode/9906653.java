    public void run() {
        while (active) {
            String line = engine.modeHackVector.getString();
            StringTokenizer st = new StringTokenizer(line);
            System.out.println("Mam mode hack: " + line);
            if (line.startsWith(engine.getNick())) continue;
            if (st.countTokens() < 5) continue;
            String mask = st.nextToken().toLowerCase();
            st.nextToken();
            String ch = st.nextToken();
            Channel chan = engine.getChannel(ch);
            if (chan == null) chan = new Channel();
            if (line.indexOf("#") == -1 && ch.indexOf("!") == -1) continue;
            String mode = line.substring(line.indexOf("#"));
            mode = mode.substring(mode.indexOf(" ")).trim();
            st = new StringTokenizer(mode);
            String mods = st.nextToken();
            int index;
            boolean plus = false;
            boolean action_I = false;
            boolean action_e = false;
            while (mods.length() > 0) {
                if (mods.startsWith("+")) {
                    plus = true;
                    mods = mods.substring(1);
                } else if (mods.startsWith("-")) {
                    plus = false;
                    mods = mods.substring(1);
                } else if (mods.startsWith("l")) {
                    mods = mods.substring(1);
                    if (plus) {
                        st.nextToken();
                        engine.getLimitThread().addChannel(ch);
                    } else engine.getLimitThread().removeChannel(ch);
                } else if (mods.startsWith("o")) {
                    mods = mods.substring(1);
                    String kogo = "dummy";
                    if (st.hasMoreTokens()) kogo = st.nextToken(); else {
                        mods = "";
                        continue;
                    }
                    if (kogo.toLowerCase().equals(engine.getNick().toLowerCase())) engine.IamOP.add(ch.toLowerCase()); else {
                        LUser luser = chan.getUser(kogo);
                        if (luser == null) luser = new LUser(kogo, "--", "--");
                        if (!plus && luser.isFlag(ch, 'O') && engine.IamOP.get(ch.toLowerCase()) != null) {
                            if (engine.writeActionLine("MODE " + ch + " +o " + kogo)) engine.LastMode_chan = ch;
                            System.out.println("Zrobilem: " + "MODE " + ch + " +o " + kogo);
                        } else if (plus && !luser.isFlag(ch, 'O') && chan.isFlag("bitch") && engine.IamOP.get(ch.toLowerCase()) != null) {
                        }
                    }
                } else if (mods.startsWith("I")) {
                    mods = mods.substring(1);
                    action_I = true;
                    if (st.hasMoreTokens()) st.nextToken();
                } else if (mods.startsWith("e")) {
                    mods = mods.substring(1);
                    action_e = true;
                    if (st.hasMoreTokens()) st.nextToken();
                } else if (mods.startsWith("b")) {
                    mods = mods.substring(1);
                    if (st.hasMoreTokens()) st.nextToken();
                } else if (mods.startsWith("v")) {
                    mods = mods.substring(1);
                    if (st.hasMoreTokens()) st.nextToken();
                } else if (mods.length() > 0) {
                    mods = mods.substring(1);
                }
            }
            if (action_I && engine.MODE < 2) ((ChanInvitationThread) engine.getChanInvitationThread()).query(ch);
            if (action_e && engine.MODE < 2) ((ChanExceptionThread) engine.getChanExceptionThread()).query(ch);
        }
    }
