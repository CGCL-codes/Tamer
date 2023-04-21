    public LinkedList<Link> update(ArrayList<ACLOperation> ACLUpdates) {
        final int LEVEL_NONE = 0;
        final int LEVEL_READ = 1;
        final int LEVEL_WRITE = 2;
        final int LEVEL_MANAGE = 3;
        TreeMap<Link, Integer> tm = new TreeMap<Link, Integer>(_comparator);
        for (ACLOperation op : ACLUpdates) {
            int levelOld = LEVEL_NONE;
            if (_readers.contains(op)) {
                levelOld = LEVEL_READ;
            } else if (_writers.contains(op)) {
                levelOld = LEVEL_WRITE;
            } else if (_managers.contains(op)) {
                levelOld = LEVEL_MANAGE;
            }
            if (ACLOperation.LABEL_ADD_READER.equals(op.targetLabel())) {
                addReader(op);
            } else if (ACLOperation.LABEL_ADD_WRITER.equals(op.targetLabel())) {
                addWriter(op);
            } else if (ACLOperation.LABEL_ADD_MANAGER.equals(op.targetLabel())) {
                addManager(op);
            } else if (ACLOperation.LABEL_DEL_READER.equals(op.targetLabel())) {
                removeReader(op);
            } else if (ACLOperation.LABEL_DEL_WRITER.equals(op.targetLabel())) {
                removeWriter(op);
            } else if (ACLOperation.LABEL_DEL_MANAGER.equals(op.targetLabel())) {
                removeManager(op);
            }
            if (!tm.containsKey(op)) {
                tm.put(op, levelOld);
            }
        }
        boolean newKeyRequired = false;
        LinkedList<Link> newReaders = new LinkedList<Link>();
        Iterator<Link> it = tm.keySet().iterator();
        while (it.hasNext()) {
            Link p = it.next();
            int lvOld = tm.get(p);
            if (_readers.contains(p) || _writers.contains(p) || _managers.contains(p)) {
                if (lvOld == LEVEL_NONE) {
                    newReaders.add(p);
                }
            } else if (lvOld > LEVEL_NONE) {
                newKeyRequired = true;
            }
        }
        if (newKeyRequired) {
            return null;
        }
        return newReaders;
    }
