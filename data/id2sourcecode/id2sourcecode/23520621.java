    public void perform(Session session, CommandArguments args, Log out) {
        if (!session.isActive()) {
            throw new CommandException(Bundle.getString("noActiveSession"));
        }
        ContextManager conman = (ContextManager) session.getManager(ContextManager.class);
        ThreadReference current = conman.getCurrentThread();
        if (args.hasMoreTokens()) {
            long tid = -1;
            String name = args.nextToken();
            try {
                tid = Long.parseLong(name);
            } catch (NumberFormatException nfe) {
            }
            List threadsList = null;
            Iterator iter = new ThreadGroupIterator(session.getVM().topLevelThreadGroups());
            if (tid > -1) {
                while (iter.hasNext()) {
                    ThreadGroupReference group = (ThreadGroupReference) iter.next();
                    if (group.uniqueID() == tid) {
                        threadsList = group.threads();
                        break;
                    }
                }
            } else {
                threadsList = new ArrayList();
                Pattern patt = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
                while (iter.hasNext()) {
                    ThreadGroupReference group = (ThreadGroupReference) iter.next();
                    Matcher matcher = patt.matcher(group.name());
                    if (matcher.find()) {
                        threadsList.addAll(group.threads());
                    } else {
                        String idstr = String.valueOf(group.uniqueID());
                        matcher = patt.matcher(idstr);
                        if (matcher.find()) {
                            threadsList.addAll(group.threads());
                        }
                    }
                }
            }
            if (threadsList == null || threadsList.size() == 0) {
                out.writeln(Bundle.getString("threads.noThreadsInGroup"));
            } else if (threadsList.size() > 0) {
                out.write(printThreads(threadsList.iterator(), "  ", current));
            }
        } else {
            List topGroups = session.getVM().topLevelThreadGroups();
            if (topGroups == null || topGroups.size() == 0) {
                out.writeln(Bundle.getString("threads.noThreads"));
            } else if (topGroups.size() > 0) {
                Iterator iter = topGroups.iterator();
                while (iter.hasNext()) {
                    ThreadGroupReference group = (ThreadGroupReference) iter.next();
                    printGroup(group, current, out, "");
                }
            }
        }
    }
