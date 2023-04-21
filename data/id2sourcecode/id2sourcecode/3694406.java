        public void run() {
            String inline;
            try {
                c = b.dccSendChatRequest(other, 120000);
                c.accept();
                c.sendLine("IRCG V0.01a");
                c.sendLine("Welcome to the party!");
                c.sendLine("[\\]");
                while ((inline = c.readLine()) != null) {
                    inline = inline.trim();
                    if (inline.length() == 0) continue;
                    String[] parts = inline.split("\\s+", 2);
                    if (parts[0].equalsIgnoreCase("exit")) {
                        c.close();
                        return;
                    } else if (parts[0].equalsIgnoreCase("help")) {
                        if (parts.length == 2) {
                            c.sendLine("There is no help for this command");
                        } else {
                            c.sendLine("Understood commands:");
                            c.sendLine("HELP   Lists all available commands");
                            c.sendLine("DIR    Lists all files in the current directory");
                            c.sendLine("CD     Changes the current directory");
                            c.sendLine("GET    Queues a file for retrival");
                            c.sendLine("STATS  Shows the stats of the selected fserv");
                            c.sendLine("FIND   Finds a file on this server");
                            c.sendLine("GFIND  Finds a file on all servers");
                            c.sendLine("QUEUES Lists all queued transfers");
                            c.sendLine("SENDS  Lists all ongoing transfers");
                            c.sendLine("DUMPDB Sends a list with all db entries");
                        }
                    } else if (parts[0].equalsIgnoreCase("dumpdb")) {
                        File tmp = File.createTempFile("dbdump", ".xml");
                        tmp.deleteOnExit();
                        dumpServers(tmp);
                        c.sendLine("Get ready to receive the file!");
                        b.dccSendFile(tmp, other, 120000);
                    } else if (parts[0].equalsIgnoreCase("queues")) {
                        QueueHolder qh = serversettings.get(b.getServer() + ":" + b.getPort()).queues;
                        synchronized (qh.queuedQueue) {
                            c.sendLine("Queued Files:");
                            c.sendLine("Remotely queued:");
                            for (QueueElement qe : qh.queuedQueue) {
                                c.sendLine(qe.toString());
                            }
                        }
                        synchronized (qh.waitQueue) {
                            c.sendLine("Locally queued:");
                            for (QueueElement qe : qh.waitQueue) {
                                c.sendLine(qe.toString());
                            }
                        }
                    } else if (parts[0].equalsIgnoreCase("sends")) {
                        QueueHolder qh = serversettings.get(b.getServer() + ":" + b.getPort()).queues;
                        synchronized (qh.sendQueue) {
                            c.sendLine("Ongoing Transfers:");
                            for (QueueElement qe : qh.sendQueue) {
                                c.sendLine(qe.toString());
                            }
                        }
                    } else if (parts[0].equalsIgnoreCase("get")) {
                        if (parts.length == 1) {
                            c.sendLine("Please specify a file for GET");
                        } else if (parts[1].startsWith("\\")) {
                            c.sendLine("Absolute GETs are not yet supported");
                        } else if (parts[1].indexOf("\\") != -1) {
                            c.sendLine("Relative GETs(that span directories) are not yet supported");
                        } else {
                            QueueHolder qh = serversettings.get(server).queues;
                            FFile cld = null;
                            String udc = parts[1].trim();
                            if (isFserv) {
                                if (dirlvl < 3) {
                                    c.sendLine("Please specify a file or directory (and not a whole server, channel or trigger");
                                    continue;
                                }
                                if (dirlvl == 3) {
                                    System.err.println(udc);
                                    for (FFile f : fsi.rootfiles) {
                                        System.err.println(f.getName());
                                        if (f.getName().equals(udc)) {
                                            System.err.print(".");
                                            cld = f;
                                            break;
                                        }
                                    }
                                } else for (FFile f : directory.getChildren()) if (f.getName().equals(udc)) {
                                    cld = f;
                                    break;
                                }
                                if (cld != null) {
                                    LinkedList<QueueElement> res = new LinkedList<QueueElement>();
                                    recget(res, cld, trigger);
                                    synchronized (qh.waitQueue) {
                                        qh.waitQueue.addAll(res);
                                    }
                                    c.sendLine("File(s) sucessfully queued");
                                } else {
                                    c.sendLine("File not found");
                                }
                            } else {
                                if (dirlvl != 3) {
                                    c.sendLine("Please specify a file (and not a whole server, channel or pack");
                                    continue;
                                }
                                for (FFile f : pack.files.keySet()) if (f.getName().equals(udc)) synchronized (qh.waitQueue) {
                                    QueueElement qe = new QueueElement(f, pack);
                                    qh.waitQueue.add(qe);
                                }
                            }
                        }
                    } else if (parts[0].equalsIgnoreCase("stats")) {
                        if (dirlvl < 1) {
                            c.sendLine("Please CD to the target server first!");
                            continue;
                        }
                        if (parts.length == 1) {
                            c.sendLine("Please specify the fserv to list!");
                            continue;
                        }
                        Trigger tmp = null;
                        String udc = unescape(parts[1]);
                        for (Trigger t : serversettings.get(server).triggers) if (t.toString().equals(udc) && t.getChannel().equals(channel)) tmp = t;
                        if (tmp != null) {
                            FservInf tfsi = tmp.getFServInformation();
                            BufferedReader bru = new BufferedReader(new StringReader(tfsi.toString()));
                            String inlt;
                            while ((inlt = bru.readLine()) != null) c.sendLine(inlt);
                            bru.close();
                        } else {
                            c.sendLine("this Trigger / fserv does not exist");
                        }
                    } else if (parts[0].equalsIgnoreCase("dir")) {
                        c.sendLine("IRCG V0.01a");
                        HashSet<String> reslines = new HashSet<String>();
                        if (parts.length == 2 && parts[1].indexOf("\\") != -1) {
                            c.sendLine("The pattern may not contain directory paths");
                            continue;
                        }
                        String pts = parts.length > 1 ? parts[1] : ".*";
                        Matcher m;
                        try {
                            m = Pattern.compile(pts).matcher("");
                        } catch (PatternSyntaxException e) {
                            c.sendLine("The Pattern that you specified did not compile");
                            continue;
                        }
                        if (pts.equals(".*")) pts = "*.*";
                        String outline, uesc;
                        switch(dirlvl) {
                            case 0:
                                c.sendLine("[\\" + pts + "]");
                                for (String key : serversettings.keySet()) {
                                    m.reset(key);
                                    if (m.matches()) c.sendLine(escape(key));
                                }
                                break;
                            case 1:
                                c.sendLine("[" + pathForLvl(dirlvl) + "\\" + pts + "]");
                                for (String channel : getChannels(server)) {
                                    m.reset(channel);
                                    if (m.matches()) c.sendLine(escape(channel));
                                }
                                break;
                            case 2:
                                c.sendLine("[" + pathForLvl(dirlvl) + "\\" + pts + "]");
                                for (Trigger trigger : getTriggers(server, channel)) {
                                    m.reset(uesc = trigger.toString());
                                    if (m.matches()) c.sendLine(escape(uesc));
                                }
                                break;
                            case 3:
                                c.sendLine("[" + pathForLvl(dirlvl) + "\\" + pts + "]");
                                if (isFserv) for (FFile file : fsi.rootfiles) {
                                    m.reset(uesc = file.getName());
                                    if (m.matches()) c.sendLine(uesc);
                                } else for (FFile file : pack.files.keySet()) {
                                    m.reset(uesc = file.getName());
                                    if (m.matches()) c.sendLine(uesc);
                                }
                                break;
                            default:
                                c.sendLine("[" + pathForLvl(dirlvl) + "\\" + pts + "]");
                                for (FFile f : directory.getChildren()) {
                                    m.reset(uesc = f.getName());
                                    if (m.matches()) c.sendLine(uesc);
                                }
                        }
                        c.sendLine("End of list");
                    } else if (parts[0].equalsIgnoreCase("find")) {
                        HashMap<Trigger, LinkedList<FFile>> res = find(b.getServer() + ":" + b.getPort(), parts[1]);
                        c.sendLine("IRCG V0.01a");
                        c.sendLine("search for " + parts[1]);
                        for (Trigger t : res.keySet()) {
                            LinkedList<FFile> files = res.get(t);
                            c.sendLine(t.getUser() + "@" + t.getChannel() + ": " + t.getTrigger());
                            c.sendLine("==========");
                            for (FFile f : files) c.sendLine(f.toString());
                            c.sendLine("----------");
                        }
                        c.sendLine("End of results");
                    } else if (parts[0].equalsIgnoreCase("gfind")) {
                        HashMap<String, HashMap<Trigger, LinkedList<FFile>>> res = gfind(parts[1]);
                        c.sendLine("IRCG V0.01a");
                        c.sendLine("search for " + parts[1]);
                        for (String s : res.keySet()) {
                            HashMap<Trigger, LinkedList<FFile>> tres = res.get(s);
                            c.sendLine("Server: " + s);
                            c.sendLine("<========>");
                            for (Trigger t : tres.keySet()) {
                                LinkedList<FFile> files = tres.get(t);
                                c.sendLine(t.getUser() + "@" + t.getChannel() + ": " + t.getTrigger());
                                c.sendLine("==========");
                                for (FFile f : files) c.sendLine(f.toString());
                                c.sendLine("----------");
                            }
                        }
                        c.sendLine("End of results");
                    } else if (parts[0].equalsIgnoreCase("cd")) {
                        c.sendLine("IRCG V0.01a");
                        String outline;
                        if (parts[1].indexOf("\\") != -1) {
                            if (parts[1].startsWith("\\")) {
                                boolean exists = true;
                                String oc = channel, os = server;
                                int odlvl = dirlvl;
                                Trigger ot = trigger;
                                FservInf ofsi = fsi;
                                FFile odir = directory;
                                XDCCPack opk = pack;
                                boolean oifs = isFserv;
                                dirlvl = 0;
                                String[] dp = parts[1].substring(1).split("\\\\", 4);
                                String udc;
                                if (dp.length != 1 || dp[0].trim().length() != 0) if (dp.length > 0) {
                                    udc = unescape(dp[0]);
                                    if (serversettings.containsKey(udc)) {
                                        server = udc;
                                        dirlvl++;
                                        if (dp.length > 1) {
                                            udc = unescape(dp[1]);
                                            if (getChannels(server).contains(udc)) {
                                                channel = udc;
                                                dirlvl++;
                                                if (dp.length > 2) {
                                                    trigger = null;
                                                    pack = null;
                                                    for (Trigger t : serversettings.get(server).triggers) if (t.toString().equals(udc = unescape(dp[2])) && t.getChannel().equals(channel)) trigger = t;
                                                    if (trigger != null) {
                                                        fsi = trigger.getFServInformation();
                                                        dirlvl++;
                                                        if (dp.length > 3) {
                                                            udc = unescape(dp[3]);
                                                            if (udc.endsWith("\\")) udc = udc.substring(0, udc.length());
                                                            directory = null;
                                                            for (FFile f : fsi.files) if (f.toString().equals(udc)) directory = f;
                                                            if (directory != null && directory.isDirectory()) {
                                                                isFserv = true;
                                                                for (FFile tmp = directory; tmp != null; tmp = tmp.getParent()) dirlvl++;
                                                            } else exists = false;
                                                        }
                                                    } else {
                                                        for (XDCCPack p : serversettings.get(server).xdccpacks.values()) if (p.packname.equals(udc = unescape(dp[2])) && p.channel.equals(channel)) pack = p;
                                                        if (pack != null) {
                                                            dirlvl++;
                                                            isFserv = false;
                                                            if (dp.length > 3) exists = false;
                                                        } else exists = false;
                                                    }
                                                }
                                            } else exists = false;
                                        }
                                    } else exists = false;
                                }
                                if (exists) {
                                    c.sendLine("[" + pathForLvl(dirlvl) + "]");
                                } else {
                                    channel = oc;
                                    server = os;
                                    dirlvl = odlvl;
                                    fsi = ofsi;
                                    trigger = ot;
                                    directory = odir;
                                    pack = opk;
                                    isFserv = oifs;
                                    c.sendLine("File not found");
                                }
                            } else {
                                c.sendLine("File not found(relative addressing (with \\) not yet supported)");
                            }
                        } else if (parts[1].equals("..")) {
                            if (dirlvl > 0) {
                                dirlvl--;
                                if (dirlvl > 3) directory = directory.getParent();
                                c.sendLine("[" + pathForLvl(dirlvl) + "]");
                            } else c.sendLine("File not found");
                        } else {
                            String udc;
                            switch(dirlvl) {
                                case 0:
                                    udc = unescape(parts[1]);
                                    if (serversettings.containsKey(udc)) {
                                        server = udc;
                                        dirlvl++;
                                        c.sendLine("[" + pathForLvl(dirlvl) + "]");
                                    } else {
                                        c.sendLine("File not found");
                                    }
                                    break;
                                case 1:
                                    udc = unescape(parts[1]);
                                    if (getChannels(server).contains(udc)) {
                                        channel = udc;
                                        dirlvl++;
                                        c.sendLine("[" + pathForLvl(dirlvl) + "]");
                                    } else {
                                        c.sendLine("File not found");
                                    }
                                    break;
                                case 2:
                                    udc = unescape(parts[1]);
                                    trigger = null;
                                    for (Trigger t : serversettings.get(server).triggers) if (t.toString().equals(udc) && t.getChannel().equals(channel)) trigger = t;
                                    if (trigger != null) {
                                        fsi = trigger.getFServInformation();
                                        dirlvl++;
                                        isFserv = true;
                                        c.sendLine("[" + pathForLvl(dirlvl) + "]");
                                    } else {
                                        for (XDCCPack p : serversettings.get(server).xdccpacks.values()) if (p.packname.equals(udc) && p.channel.equals(channel)) pack = p;
                                        if (pack != null) {
                                            dirlvl++;
                                            isFserv = false;
                                            c.sendLine("[" + pathForLvl(dirlvl) + "]");
                                        } else c.sendLine("File not found");
                                    }
                                    break;
                                case 3:
                                    udc = unescape(parts[1]);
                                    directory = null;
                                    for (FFile f : fsi.rootfiles) if (f.getName().equals(udc)) directory = f;
                                    if (directory != null && directory.isDirectory()) {
                                        dirlvl++;
                                        c.sendLine("[" + pathForLvl(dirlvl) + "]");
                                    } else {
                                        c.sendLine("File not found");
                                    }
                                    break;
                                default:
                                    udc = unescape(parts[1]);
                                    FFile tmp = null;
                                    for (FFile f : directory.getChildren()) if (f.getName().equals(udc)) tmp = f;
                                    if (tmp != null && tmp.isDirectory()) {
                                        directory = tmp;
                                        dirlvl++;
                                        c.sendLine("[" + pathForLvl(dirlvl) + "]");
                                    } else {
                                        c.sendLine("File not found");
                                    }
                                    break;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
