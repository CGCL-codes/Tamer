    void insert(EventNote newNote) {
        if (isEmpty()) {
            super.insert(newNote);
            newNote.setConnected(false);
            return;
        }
        TimeInstant refTime = newNote.getTime();
        int firstIndexForInsert, lastIndexForInsert;
        int left = 0;
        int right = eTreeList.size();
        while (left < right) {
            int middle = (left + right) / 2;
            if (TimeInstant.isBefore(((EventNote) eTreeList.get(middle)).getTime(), refTime)) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        if (right < eTreeList.size() && TimeInstant.isEqual(((EventNote) eTreeList.get(right)).getTime(), refTime)) {
            firstIndexForInsert = right;
            lastIndexForInsert = findLastIndex(firstIndexForInsert) + 1;
        } else {
            firstIndexForInsert = right;
            lastIndexForInsert = firstIndexForInsert;
        }
        if (firstIndexForInsert != lastIndexForInsert) {
            firstIndexForInsert += _positionGenerator.nextInt(lastIndexForInsert - firstIndexForInsert + 1);
            while (firstIndexForInsert < this.eTreeList.size() && ((EventNote) eTreeList.get(firstIndexForInsert)).isConnected()) firstIndexForInsert++;
        }
        this.eTreeList.add(firstIndexForInsert, newNote);
        newNote.setConnected(false);
    }
