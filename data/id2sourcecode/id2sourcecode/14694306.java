    protected static void generateEvidence(IndentWriter writer) {
        writer.println(file_map.size() + " FMFile Reservations");
        try {
            writer.indent();
            try {
                file_map_mon.enter();
                Iterator it = file_map.keySet().iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    List owners = (List) file_map.get(key);
                    Iterator it2 = owners.iterator();
                    String str = "";
                    while (it2.hasNext()) {
                        Object[] entry = (Object[]) it2.next();
                        FMFileOwner owner = (FMFileOwner) entry[0];
                        Boolean write = (Boolean) entry[1];
                        String reason = (String) entry[2];
                        str += (str.length() == 0 ? "" : ", ") + owner.getName() + "[" + (write.booleanValue() ? "write" : "read") + "/" + reason + "]";
                    }
                    writer.println(Debug.secretFileName(key) + " -> " + str);
                }
            } finally {
                file_map_mon.exit();
            }
            FMFileManagerImpl.generateEvidence(writer);
        } finally {
            writer.exdent();
        }
    }
