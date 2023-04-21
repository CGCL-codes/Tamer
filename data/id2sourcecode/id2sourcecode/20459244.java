    public int getPositionForSection(int sectionIndex) {
        final SparseIntArray stringMap = mStringMap;
        final Cursor cursor = mDataCursor;
        if (cursor == null || sectionIndex <= 0) {
            return 0;
        }
        if (sectionIndex >= sSectionsLength) {
            sectionIndex = sSectionsLength - 1;
        }
        int savedCursorPos = cursor.getPosition();
        String targetLetter = sSections[sectionIndex];
        int key = targetLetter.codePointAt(0);
        {
            int tmp = stringMap.get(key, Integer.MIN_VALUE);
            if (Integer.MIN_VALUE != tmp) {
                return tmp;
            }
        }
        int end = cursor.getCount();
        int pos = 0;
        {
            int prevLetter = sSections[sectionIndex - 1].codePointAt(0);
            int prevLetterPos = stringMap.get(prevLetter, Integer.MIN_VALUE);
            if (prevLetterPos != Integer.MIN_VALUE) {
                pos = prevLetterPos;
            }
        }
        while (end - pos > 100) {
            int tmp = (end + pos) / 2;
            cursor.moveToPosition(tmp);
            String sort_name;
            do {
                sort_name = cursor.getString(mColumnIndex);
                if (sort_name == null || sort_name.length() == 0) {
                    Log.e(TAG, "sort_name is null or its length is 0. index: " + tmp);
                    cursor.moveToNext();
                    tmp++;
                    continue;
                }
                break;
            } while (tmp < end);
            if (tmp == end) {
                break;
            }
            int codePoint = sort_name.codePointAt(0);
            if (codePoint < getSectionCodePoint(sectionIndex)) {
                pos = tmp;
            } else {
                end = tmp;
            }
        }
        for (cursor.moveToPosition(pos); !cursor.isAfterLast(); ++pos, cursor.moveToNext()) {
            String sort_name = cursor.getString(mColumnIndex);
            if (sort_name == null || sort_name.length() == 0) {
                Log.e(TAG, "sort_name is null or its length is 0. index: " + pos);
                continue;
            }
            int codePoint = sort_name.codePointAt(0);
            if (codePoint >= getSectionCodePoint(sectionIndex)) {
                break;
            }
        }
        stringMap.put(key, pos);
        cursor.moveToPosition(savedCursorPos);
        return pos;
    }
