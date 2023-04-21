    void call(RestoreSettings settings) throws Exception {
        File dbdir = new File(settings.source.get());
        String dbpath = dbdir.getPath() + File.separator + dbdir.getName() + ".db.gz";
        String datapath = dbdir.getPath();
        DataBase database = settings.database;
        if (database == null) database = Common.loadDataBase(dbpath);
        File target = new File(settings.target.get());
        if (!target.exists()) if (!target.mkdirs()) throw new RuntimeException("can't create target path");
        if (!target.canWrite()) throw new RuntimeException("can't write in target path");
        String base = target.getPath() + File.separator;
        boolean hashall = settings.hashall;
        DataFileReader reader = new DataFileReader(new File(datapath), dbdir.getName());
        Repository lastRep = settings.repository;
        if (lastRep == null) lastRep = database.repository(database.repositories() - 1);
        SortedMap<Reference, String> map = settings.restoreSet;
        if (map == null) {
            map = new TreeMap<Reference, String>();
            for (String path : database.entryPaths()) {
                List<Reference> refs = database.pathEntry(path);
                Reference ref = refs.get(refs.size() - 1);
                if (!ref.repositories.contains(lastRep)) continue;
                map.put(ref, path);
            }
        }
        Repository curRep = lastRep;
        for (Entry<Reference, String> entry : map.entrySet()) {
            String path = entry.getValue();
            Reference ref = entry.getKey();
            if (ref.data == null) {
                boolean created = new File(base + path).mkdirs();
                if (Debug.level > 1 && !created) System.err.println("problem creating directory " + base + path);
                continue;
            }
            if (ref.data.repository != curRep) {
                curRep = ref.data.repository;
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setDialogType(JFileChooser.OPEN_DIALOG);
                chooser.setDialogTitle("please choose " + curRep.name);
                chooser.setCurrentDirectory(new File(datapath).getParentFile());
                chooser.setMultiSelectionEnabled(false);
                if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) throw new RuntimeException("restore aborted by user");
                datapath = chooser.getSelectedFile().getPath();
                reader = new DataFileReader(new File(datapath), new File(datapath).getName());
            }
            writeAndCheckFile(reader, base, path, database.hash(database.dataIndex(ref.data)), ref, hashall);
        }
    }
