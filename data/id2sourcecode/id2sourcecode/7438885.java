    @Override
    public void perform(CommandContext context, CommandArguments arguments) throws CommandException, MissingArgumentsException {
        Session session = context.getSession();
        PrintWriter writer = context.getWriter();
        VirtualMachine vm = session.getConnection().getVM();
        if (arguments.hasMoreTokens()) {
            long tid = -1;
            String name = arguments.nextToken();
            try {
                tid = Long.parseLong(name);
            } catch (NumberFormatException nfe) {
            }
            List<ThreadGroupReference> groupsList = null;
            Iterator<ThreadGroupReference> iter = Threads.iterateGroups(vm.topLevelThreadGroups());
            if (tid > -1) {
                while (iter.hasNext()) {
                    ThreadGroupReference group = iter.next();
                    if (group.uniqueID() == tid) {
                        groupsList = group.threadGroups();
                        break;
                    }
                }
            } else {
                groupsList = new ArrayList<ThreadGroupReference>();
                Pattern patt = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
                while (iter.hasNext()) {
                    ThreadGroupReference group = iter.next();
                    Matcher matcher = patt.matcher(group.name());
                    if (matcher.find()) {
                        groupsList.addAll(group.threadGroups());
                    } else {
                        String idstr = String.valueOf(group.uniqueID());
                        matcher = patt.matcher(idstr);
                        if (matcher.find()) {
                            groupsList.addAll(group.threadGroups());
                        }
                    }
                }
            }
            if (groupsList == null || groupsList.size() == 0) {
                writer.println(NbBundle.getMessage(getClass(), "CTL_threadgroups_NoGroupsInGroup"));
            } else if (groupsList.size() > 0) {
                iter = groupsList.iterator();
                while (iter.hasNext()) {
                    ThreadGroupReference group = iter.next();
                    printGroup(group, writer, "");
                }
            }
        } else {
            List topGroups = vm.topLevelThreadGroups();
            if (topGroups == null || topGroups.size() == 0) {
                writer.println(NbBundle.getMessage(getClass(), "CTL_threadgroups_NoGroups"));
            } else if (topGroups.size() > 0) {
                Iterator iter = topGroups.iterator();
                while (iter.hasNext()) {
                    ThreadGroupReference group = (ThreadGroupReference) iter.next();
                    printGroup(group, writer, "");
                }
            }
        }
    }
