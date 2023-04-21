    protected void printGroup(ThreadGroupReference group, ThreadReference current, Log out, String prefix) {
        ReferenceType clazz = group.referenceType();
        String id = String.valueOf(group.uniqueID());
        if (clazz == null) {
            out.writeln(prefix + id + ' ' + group.name());
        } else {
            out.writeln(prefix + id + ' ' + group.name() + " (" + clazz.name() + ')');
        }
        List groups = group.threadGroups();
        Iterator iter = groups.iterator();
        while (iter.hasNext()) {
            ThreadGroupReference subgrp = (ThreadGroupReference) iter.next();
            printGroup(subgrp, current, out, prefix + "  ");
        }
        List threads = group.threads();
        out.write(printThreads(threads.iterator(), prefix + "  ", current));
    }
