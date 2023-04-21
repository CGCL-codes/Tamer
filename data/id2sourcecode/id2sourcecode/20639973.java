    public boolean addNote(Note newNote) {
        if (!notes.contains(newNote)) {
            notes.add(newNote);
            if (!channels.contains(new Integer(newNote.getChannel()))) {
                channels.add(newNote.getChannel());
            }
            return true;
        }
        return false;
    }
