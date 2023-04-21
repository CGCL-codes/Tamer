    public static void saveMemento(Memento memento, OutputStream out) throws IOException {
        ZipOutputStream zout = new ZipOutputStream(out);
        zout.putNextEntry(new ZipEntry(ENTRY_INFO));
        Properties info = new Properties();
        info.put(INFO_VERSION, Integer.toString(2));
        info.put(INFO_NAME, memento.getName());
        info.put(INFO_ADD_INDEX, Integer.toString(memento.getAddIndex()));
        info.put(INFO_SHIFT, Integer.toString(memento.getShift()));
        info.put(INFO_VISIBLE_COLUMNS, Joiner.on(",").join(memento.getVisibleColumns()));
        info.store(zout, "Info file");
        zout.putNextEntry(new ZipEntry(ENTRY_NOTES));
        TreeMap<Integer, Note> notes = memento.getNotes();
        Properties notesP = new Properties();
        for (Integer index : notes.keySet()) {
            notesP.put(index.toString(), notes.get(index).getNote());
        }
        notesP.store(zout, null);
        zout.putNextEntry(new ZipEntry(ENTRY_MARKS));
        TreeMap<Integer, Boolean> marks = memento.getMarks();
        Properties marksP = new Properties();
        for (Integer index : marks.keySet()) {
            marksP.put(index.toString(), marks.get(index).toString());
        }
        marksP.store(zout, null);
        zout.putNextEntry(new ZipEntry(ENTRY_MARKS_COLORS));
        TreeMap<Integer, MarkerColors> marksColor = memento.getMarksColor();
        Properties marksColorP = new Properties();
        for (Integer index : marksColor.keySet()) {
            marksColorP.put(index.toString(), marksColor.get(index).toString());
        }
        marksColorP.store(zout, null);
        zout.putNextEntry(new ZipEntry(ENTRY_LOGS));
        List<LogData> list = memento.getList();
        persistanceVer2.saveLogsList(zout, list);
        zout.close();
    }
