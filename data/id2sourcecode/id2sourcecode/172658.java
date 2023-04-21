    public void processMessage(ServerProcess sp, Message m) {
        String command_key = m.getCommand().toLowerCase();
        String prefix = m.getPrefix();
        String params[] = m.getParameters();
        Integer t_cmd = (Integer) commands.get(command_key);
        {
            int endOff = prefix.indexOf('!');
            if (-1 != endOff) {
                prefix = prefix.substring(0, endOff);
            }
        }
        if (debug_traffic || null == t_cmd) {
            String t = m.toString();
            t = t.substring(0, t.length() - 2);
            if (debug_traffic) {
                System.out.println("< ".concat(t));
            }
            if (null == t_cmd) {
                if (see_everything_from_server) {
                    try {
                        Integer.parseInt(command_key);
                        StringBuffer buf = new StringBuffer(params[1]);
                        for (int i = 2; i < params.length; i++) {
                            buf.append(' ');
                            buf.append(params[i]);
                        }
                        status.printUnmangled(buf.toString());
                    } catch (NumberFormatException e) {
                        status.printUnmangled(t);
                    }
                }
                return;
            }
        }
        int command = t_cmd.intValue();
        boolean is_prefix_ignored = ignore_list.contains(prefix);
        switch(command) {
            case -1:
                {
                    String p[] = { params[0] };
                    sendMessage("pong", p);
                    break;
                }
            case -2:
                {
                    if (prefix.equals(current_nick)) {
                        current_nick = params[0];
                        nick_entry.setText(current_nick);
                        Object a[] = { current_nick };
                        String ptn = lang.getString("eirc.s4");
                        getCurrentPanel().printInfo(MessageFormat.format(ptn, a));
                    } else {
                        Object a[] = { prefix, params[0] };
                        String ptn = lang.getString("eirc.s5");
                        String msg_text = MessageFormat.format(ptn, a);
                        OutputWindow[] ow = getUserPanels(prefix);
                        for (int i = 0; i < ow.length; i++) {
                            ow[i].printInfo(msg_text);
                        }
                    }
                    String new_nick = params[0];
                    if (privates.containsKey(prefix)) {
                        if (privates.containsKey(new_nick)) {
                            ((PrivateWindow) privates.get(new_nick)).dispose();
                        }
                        PrivateWindow pw = (PrivateWindow) privates.get(prefix);
                        pw.setUser(new_nick);
                        chat_panel.rename(prefix, new_nick);
                        privates.remove(prefix);
                        privates.put(new_nick, pw);
                    }
                    for (Enumeration e = channels.elements(); e.hasMoreElements(); ) {
                        Channel channel = (Channel) e.nextElement();
                        if (channel.contains(prefix)) {
                            channel.rename(prefix, new_nick);
                        }
                    }
                    break;
                }
            case -3:
                {
                    if (current_nick.equals(prefix)) {
                        openChannel(params[0]);
                    } else {
                        Channel channel = getChannel(params[0]);
                        channel.add(prefix);
                        if (see_join) {
                            ChannelWindow cw = getChannelWindow(params[0]);
                            Object a[] = { prefix };
                            String ptn = lang.getString("eirc.s6");
                            cw.printInfo(MessageFormat.format(ptn, a));
                        }
                    }
                    break;
                }
            case -4:
                {
                    if (!Channel.isChannel(params[0])) {
                        break;
                    }
                    String[] modes_params = new String[params.length - 2];
                    for (int i = 0; i < modes_params.length; i++) {
                        modes_params[i] = params[i + 2];
                    }
                    Channel channel = getChannel(params[0]);
                    channel.setModes(params[1], modes_params);
                    ChannelWindow cw = getChannelWindow(params[0]);
                    boolean sign = false;
                    char modes[] = params[1].toCharArray();
                    int j = 0;
                    for (int i = 0; i < modes.length; i++) {
                        String msg_tag = null;
                        switch(modes[i]) {
                            case '+':
                                sign = true;
                                break;
                            case '-':
                                sign = false;
                                break;
                            case 'v':
                                msg_tag = sign ? "eirc.voice" : "eirc.unvoice";
                                break;
                            case 'o':
                                msg_tag = sign ? "eirc.op" : "eirc.deop";
                                break;
                            case 'h':
                                msg_tag = sign ? "eirc.hop" : "eirc.dehop";
                                break;
                            case 'b':
                                msg_tag = sign ? "eirc.ban" : "eirc.unban";
                                break;
                            case 'k':
                            case 'l':
                            case 'e':
                            case 'I':
                                j++;
                                break;
                        }
                        if (null != msg_tag) {
                            Object a[] = { prefix, modes_params[j] };
                            String ptn = lang.getString(msg_tag);
                            cw.printInfo(MessageFormat.format(ptn, a));
                            j++;
                        }
                    }
                    break;
                }
            case -5:
                {
                    if (current_nick.equals(prefix)) {
                        channels.removeElement(params[0]);
                        ChannelWindow cw = getChannelWindow(params[0]);
                        cw.dispose();
                    } else {
                        Channel channel = getChannel(params[0]);
                        channel.remove(prefix);
                        if (see_join) {
                            ChannelWindow cw = getChannelWindow(params[0]);
                            Object a[] = { prefix };
                            String ptn = lang.getString("eirc.s8");
                            cw.printInfo(MessageFormat.format(ptn, a));
                        }
                    }
                    break;
                }
            case -6:
                {
                    Object a[] = { prefix, params[0] };
                    String ptn = lang.getString("eirc.s9");
                    String msg_text = MessageFormat.format(ptn, a);
                    OutputWindow[] ow = getUserPanels(prefix);
                    for (int i = 0; i < ow.length; i++) {
                        ow[i].printInfo(msg_text);
                    }
                    for (Enumeration e = channels.elements(); e.hasMoreElements(); ) {
                        Channel channel = (Channel) e.nextElement();
                        channel.remove(prefix);
                    }
                    break;
                }
            case -7:
                {
                    String reason = null != params[2] ? params[2] : "";
                    if (current_nick.equals(params[1])) {
                        channels.removeElement(params[0]);
                        ChannelWindow cw = getChannelWindow(params[0]);
                        cw.dispose();
                        Object a[] = { params[0], prefix, reason };
                        String ptn = lang.getString("eirc.s10");
                        status.printInfo(MessageFormat.format(ptn, a));
                    } else {
                        Channel channel = getChannel(params[0]);
                        channel.remove(params[1]);
                        ChannelWindow cw = getChannelWindow(params[0]);
                        Object a[] = { params[1], prefix, reason };
                        String ptn = lang.getString("eirc.s11");
                        cw.printInfo(MessageFormat.format(ptn, a));
                    }
                    break;
                }
            case -8:
                {
                    Channel channel = getChannel(params[0]);
                    channel.setTopic(MircMessage.filterMircAttributes(params[1]));
                    ChannelWindow cw = getChannelWindow(params[0]);
                    Object a[] = { prefix, params[1] };
                    String ptn = lang.getString("eirc.s36");
                    cw.printInfo(MessageFormat.format(ptn, a));
                    break;
                }
            case -9:
                {
                    if (is_prefix_ignored) {
                        break;
                    }
                    boolean isChannel = Channel.isChannel(params[0]);
                    if (CTCPMessage.isCTCPMessage(params[1])) {
                        CTCPMessage ctcp = new CTCPMessage(params[1]);
                        boolean report_user = true;
                        switch(ctcp.getCommand()) {
                            case CTCPMessage.ACTION:
                                {
                                    report_user = false;
                                    if (!ctcp.hasParameter()) {
                                        break;
                                    }
                                    ChatWindow target = isChannel ? (ChatWindow) getChannelWindow(params[0]) : (ChatWindow) openPrivate(prefix);
                                    target.printAction(ctcp.getParameter(), prefix);
                                    break;
                                }
                            case CTCPMessage.PING:
                                {
                                    String param = ctcp.hasParameter() ? ctcp.getParameter() : "";
                                    CTCPMessage reply = new CTCPMessage("PING", param);
                                    String p[] = { prefix, reply.toString() };
                                    sendMessage("notice", p);
                                    break;
                                }
                            case CTCPMessage.DCC:
                                {
                                    Object a[] = { prefix };
                                    String ptn = lang.getString("eirc.dcc_not_supported");
                                    OutputWindow target = getCurrentPanel();
                                    target.printInfo(MessageFormat.format(ptn, a));
                                    if (on_dcc_notify_peer) {
                                        String p[] = { prefix, lang.getString("eirc.dcc_notify.remote") };
                                        sendMessage("notice", p);
                                        ptn = lang.getString("eirc.dcc_notify.local");
                                        target.printInfo(MessageFormat.format(ptn, a));
                                    }
                                    break;
                                }
                            case CTCPMessage.VERSION:
                                {
                                    CTCPMessage reply = new CTCPMessage("VERSION", PACKAGE.concat("-").concat(VERSION).concat(VERSION_EXTRA).concat(" ").concat(AUTHOR));
                                    String p[] = { prefix, reply.toString() };
                                    sendMessage("notice", p);
                                    break;
                                }
                        }
                        if (report_user) {
                            Object[] a = { ctcp.getCommandString(), prefix };
                            String ptn = lang.getString("eirc.ctcp_received");
                            status.printWarning(MessageFormat.format(ptn, a));
                        }
                    } else {
                        ChatWindow target = isChannel ? (ChatWindow) getChannelWindow(params[0]) : (ChatWindow) openPrivate(prefix);
                        target.printPrivmsg(params[1], prefix);
                    }
                    break;
                }
            case -10:
                {
                    if (is_prefix_ignored) {
                        break;
                    }
                    if (prefix.length() > 0) {
                        int endOff = prefix.indexOf('!');
                        if (-1 != endOff) {
                            prefix = prefix.substring(0, endOff);
                        }
                    }
                    if (Channel.isChannel(params[0])) {
                        getChannelWindow(params[0]).printNotice(params[1], prefix);
                    } else if (special_services && 0 != prefix.length() && -1 != service_bots.indexOf(prefix)) {
                        openPrivate(prefix).printNotice(params[1], prefix);
                    } else if (params[0].equals(current_nick)) {
                        if (!CTCPMessage.isCTCPMessage(params[1])) {
                            getCurrentPanel().printNotice(params[1], prefix);
                        } else {
                            CTCPMessage ctcp = new CTCPMessage(params[1]);
                            switch(ctcp.getCommand()) {
                                case CTCPMessage.PING:
                                    {
                                        double diff = Double.NEGATIVE_INFINITY;
                                        if (ctcp.hasParameter()) {
                                            try {
                                                long launch = Long.parseLong(ctcp.getParameter());
                                                long arrive = (new Date()).getTime();
                                                diff = (arrive - launch) / 1000.0;
                                            } catch (NumberFormatException e) {
                                            }
                                        }
                                        {
                                            Object a[] = { prefix, new Double(diff) };
                                            MessageFormat mf = new MessageFormat(lang.getString("eirc.s12"));
                                            double limits[] = { ChoiceFormat.previousDouble(0.0), 0.0, 1.0, ChoiceFormat.nextDouble(1.0) };
                                            String times[] = { lang.getString("eirc.s12.0"), lang.getString("eirc.s12.1"), lang.getString("eirc.s12.2"), lang.getString("eirc.s12.3") };
                                            mf.setFormat(1, new ChoiceFormat(limits, times));
                                            getCurrentPanel().printInfo(mf.format(a));
                                        }
                                        break;
                                    }
                                default:
                                    {
                                        Object a[] = { ctcp.getCommandString(), ctcp.getParameter() };
                                        String ptn = lang.getString("eirc.ctcp_reply");
                                        getCurrentPanel().printNotice(MessageFormat.format(ptn, a), prefix);
                                        break;
                                    }
                            }
                        }
                    } else {
                        status.printNotice(params[0] + " " + params[1], prefix);
                    }
                    break;
                }
            case -11:
                {
                    status.printError(params[0]);
                    break;
                }
            case 301:
                {
                    Object a[] = { params[1], params[2] };
                    String ptn = lang.getString("eirc.301");
                    getUserPanel(params[1]).printInfo(MessageFormat.format(ptn, a));
                    break;
                }
            case 305:
            case 306:
                {
                    getCurrentPanel().printInfo(lang.getString("eirc." + command));
                    break;
                }
            case 307:
                {
                    Object a[] = { params[1] };
                    String ptn = lang.getString("eirc.s27");
                    getCurrentPanel().printInfo(MessageFormat.format(ptn, a));
                    break;
                }
            case 310:
                {
                    Object a[] = { params[1], params[2] };
                    String ptn = lang.getString("eirc.310");
                    getCurrentPanel().printInfo(MessageFormat.format(ptn, a));
                    break;
                }
            case 311:
                {
                    String t;
                    if (hideip) {
                        params[3] = "xxx";
                    }
                    {
                        Object a[] = { params[1], params[2], params[3] };
                        String ptn = lang.getString("eirc.s13");
                        getCurrentPanel().printInfo(MessageFormat.format(ptn, a), false);
                    }
                    {
                        Object a[] = { params[1], params[5] };
                        String ptn = lang.getString("eirc.s14");
                        getCurrentPanel().printInfo(MessageFormat.format(ptn, a));
                    }
                    break;
                }
            case 312:
                {
                    Object a[] = { params[1], params[2], params[3] };
                    String ptn = lang.getString("eirc.s15");
                    getCurrentPanel().printInfo(MessageFormat.format(ptn, a));
                    break;
                }
            case 313:
                {
                    Object a[] = { params[1] };
                    String ptn = lang.getString("eirc.s16");
                    getCurrentPanel().printInfo(MessageFormat.format(ptn, a));
                    break;
                }
            case 317:
                {
                    Object a[] = { params[1], new Integer(params[2]) };
                    MessageFormat mf = new MessageFormat(lang.getString("eirc.s17"));
                    double limits[] = { 0, 1, 2 };
                    String times[] = { lang.getString("eirc.s17.0"), lang.getString("eirc.s17.1"), lang.getString("eirc.s17.2") };
                    mf.setFormat(1, new ChoiceFormat(limits, times));
                    getCurrentPanel().printInfo(mf.format(a));
                    break;
                }
            case 319:
                {
                    Object a[] = { params[1], params[2] };
                    String ptn = lang.getString("eirc.s18");
                    getCurrentPanel().printInfo(MessageFormat.format(ptn, a));
                    break;
                }
            case 320:
                {
                    Object a[] = { params[1], params[2] };
                    String ptn = lang.getString("eirc.320");
                    getCurrentPanel().printInfo(MessageFormat.format(ptn, a));
                    break;
                }
            case 321:
                {
                    replyListStart();
                    break;
                }
            case 322:
                {
                    if (params[1].equals("*")) {
                        break;
                    }
                    int users = 0;
                    try {
                        users = Integer.parseInt(params[2]);
                    } catch (NumberFormatException ex) {
                    }
                    String topic = MircMessage.filterMircAttributes(params[3]);
                    replyListAdd(params[1], users, topic);
                    break;
                }
            case 323:
                {
                    replyListEnd();
                    break;
                }
            case 324:
                {
                    String[] modes_params = new String[params.length - 3];
                    for (int i = 0; i < modes_params.length; i++) {
                        modes_params[i] = params[i + 3];
                    }
                    Channel channel = getChannel(params[1]);
                    channel.setModes(params[2], modes_params);
                    break;
                }
            case 328:
                {
                    Channel channel = getChannel(params[1]);
                    channel.setUrl(params[1]);
                    ChannelWindow cw = getChannelWindow(params[1]);
                    Object a[] = { params[0], params[2] };
                    String ptn = lang.getString("eirc.328");
                    cw.printInfo(MessageFormat.format(ptn, a));
                    break;
                }
            case 331:
                {
                    Channel channel = getChannel(params[1]);
                    channel.setTopic("");
                    ChannelWindow cw = getChannelWindow(params[1]);
                    Object a[] = { params[1] };
                    String ptn = lang.getString("eirc.s37");
                    cw.printInfo(MessageFormat.format(ptn, a));
                    break;
                }
            case 332:
                {
                    Channel channel = getChannel(params[1]);
                    channel.setTopic(MircMessage.filterMircAttributes(params[2]));
                    ChannelWindow cw = getChannelWindow(params[1]);
                    Object a[] = { params[1], params[2] };
                    String ptn = lang.getString("eirc.s38");
                    cw.printInfo(MessageFormat.format(ptn, a));
                    break;
                }
            case 333:
                {
                    Date topic_date = new Date(Long.parseLong(params[3]) * 1000);
                    OutputWindow cw = getChannelWindow(params[1]);
                    Object a[] = { params[2], topic_date, topic_date };
                    String ptn = lang.getString("eirc.s35");
                    cw.printInfo(MessageFormat.format(ptn, a));
                    break;
                }
            case 335:
                {
                    if (params.length < 3) {
                        break;
                    }
                    Object a[] = { params[1], params[2] };
                    String ptn = lang.getString("eirc.335");
                    getCurrentPanel().printInfo(MessageFormat.format(ptn, a));
                    break;
                }
            case 353:
                {
                    Channel channel = getChannel(params[2]);
                    if (null == channel || channel.contains(getNick())) {
                        break;
                    }
                    StringTokenizer st = new StringTokenizer(params[3], " ");
                    int tokens = st.countTokens();
                    for (int i = 0; i < tokens; i++) {
                        channel.add(st.nextToken());
                    }
                    break;
                }
            case 372:
                {
                    status.printInfo(params[1]);
                    break;
                }
            case 376:
            case 422:
                {
                    if (!logged_in) {
                        this.logged_in = true;
                        if (!new_nick.equals(params[0])) {
                            this.new_nick = params[0];
                            nick_entry.setText(new_nick);
                            Object a[] = { new_nick };
                            String ptn = lang.getString("eirc.s4");
                            getCurrentPanel().printInfo(MessageFormat.format(ptn, a));
                        }
                        pos_login();
                    }
                    break;
                }
            case 421:
                {
                    Object[] a = { params[1] };
                    String ptn = lang.getString("eirc.421");
                    getCurrentPanel().printError(MessageFormat.format(ptn, a));
                    break;
                }
            case 432:
            case 433:
                {
                    getCurrentPanel().printError(lang.getString("eirc." + command));
                    break;
                }
            case 438:
                {
                    getCurrentPanel().printError(params[2]);
                    break;
                }
            case 464:
                {
                    status.printError(params[1]);
                    disconnect();
                    break;
                }
            case 401:
            case 471:
            case 473:
            case 474:
            case 475:
                {
                    Object a[] = { params[1] };
                    String ptn = lang.getString("eirc." + command);
                    status.printError(MessageFormat.format(ptn, a));
                    break;
                }
            case 482:
                {
                    getChannelWindow(params[1]).printError(lang.getString("eirc." + 482));
                    break;
                }
        }
    }
