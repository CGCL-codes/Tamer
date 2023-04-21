    private void printGroup(ThreadGroupReference group, PrintWriter writer, String prefix) {
        ReferenceType clazz = group.referenceType();
        String id = String.valueOf(group.uniqueID());
        if (clazz == null) {
            writer.println(prefix + id + ' ' + group.name());
        } else {
            writer.println(prefix + id + ' ' + group.name() + " (" + clazz.name() + ')');
        }
        List<ThreadGroupReference> groups = group.threadGroups();
        Iterator<ThreadGroupReference> iter = groups.iterator();
        while (iter.hasNext()) {
            ThreadGroupReference subgrp = iter.next();
            printGroup(subgrp, writer, prefix + "  ");
        }
    }
