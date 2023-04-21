    private void reserveAccess(String reason) throws FMFileManagerException {
        if (clone) {
            return;
        }
        try {
            file_map_mon.enter();
            List owners = (List) file_map.get(canonical_path);
            Object[] my_entry = null;
            if (owners == null) {
                Debug.out("reserveAccess fail");
                throw (new FMFileManagerException("File '" + canonical_path + "' has not been reserved (no entries), '" + owner.getName() + "'"));
            }
            for (Iterator it = owners.iterator(); it.hasNext(); ) {
                Object[] entry = (Object[]) it.next();
                String entry_name = ((FMFileOwner) entry[0]).getName();
                if (owner.getName().equals(entry_name)) {
                    my_entry = entry;
                }
            }
            if (my_entry == null) {
                Debug.out("reserveAccess fail");
                throw (new FMFileManagerException("File '" + canonical_path + "' has not been reserved (not found), '" + owner.getName() + "'"));
            }
            my_entry[1] = new Boolean(access_mode == FM_WRITE);
            my_entry[2] = reason;
            int read_access = 0;
            int write_access = 0;
            int write_access_lax = 0;
            TOTorrentFile my_torrent_file = owner.getTorrentFile();
            String users = "";
            for (Iterator it = owners.iterator(); it.hasNext(); ) {
                Object[] entry = (Object[]) it.next();
                FMFileOwner this_owner = (FMFileOwner) entry[0];
                if (((Boolean) entry[1]).booleanValue()) {
                    write_access++;
                    TOTorrentFile this_tf = this_owner.getTorrentFile();
                    if (my_torrent_file != null && this_tf != null && my_torrent_file.getLength() == this_tf.getLength()) {
                        write_access_lax++;
                    }
                    users += (users.length() == 0 ? "" : ",") + this_owner.getName() + " [write]";
                } else {
                    read_access++;
                    users += (users.length() == 0 ? "" : ",") + this_owner.getName() + " [read]";
                }
            }
            if (write_access > 1 || (write_access == 1 && read_access > 0)) {
                if (!COConfigurationManager.getBooleanParameter("File.strict.locking")) {
                    if (write_access_lax == write_access) {
                        return;
                    }
                }
                Debug.out("reserveAccess fail");
                throw (new FMFileManagerException("File '" + canonical_path + "' is in use by '" + users + "'"));
            }
        } finally {
            file_map_mon.exit();
        }
    }
