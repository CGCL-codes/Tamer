    void insert(EventNote newNote) {
        Entity who1 = newNote.getEntity1();
        if (who1 != null) {
            who1.addEventNote(newNote);
        }
        Entity who2 = newNote.getEntity2();
        if (who2 != null) {
            who2.addEventNote(newNote);
        }
        Entity who3 = newNote.getEntity3();
        if (who3 != null) {
            who3.addEventNote(newNote);
        }
        EventAbstract Event = newNote.getEvent();
        if (Event != null) {
            Event.addEventNote(newNote);
        }
        if (isEmpty()) {
            eTreeList.add(newNote);
            return;
        } else {
            int left = 0;
            int right = eTreeList.size() - 1;
            int index = 0;
            TimeInstant refTime = newNote.getTime();
            do {
                index = (left + right) / 2;
                if (TimeInstant.isBeforeOrEqual(((EventNote) eTreeList.get(index)).getTime(), refTime)) {
                    if (index < (eTreeList.size() - 1)) {
                        if (TimeInstant.isAfter(((EventNote) eTreeList.get(index + 1)).getTime(), refTime)) {
                            eTreeList.add(index + 1, newNote);
                            return;
                        } else {
                            left = index + 1;
                        }
                    } else {
                        eTreeList.add(newNote);
                        return;
                    }
                } else {
                    if (index > 0) {
                        if (TimeInstant.isBeforeOrEqual(((EventNote) eTreeList.get(index - 1)).getTime(), refTime)) {
                            eTreeList.add(index, newNote);
                            return;
                        } else {
                            right = index - 1;
                        }
                    } else {
                        eTreeList.add(0, newNote);
                        return;
                    }
                }
            } while ((left <= right));
            eTreeList.add(newNote);
        }
    }
