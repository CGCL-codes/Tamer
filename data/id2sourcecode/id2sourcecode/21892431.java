    private int CreateShortNameID(CFileInfo curDir, String name) {
        int filelist_size = curDir.longNameList.size();
        if (filelist_size <= 0) return 1;
        int foundNr = 0;
        int low = 0;
        int high = filelist_size - 1;
        int mid, res;
        while (low <= high) {
            mid = (low + high) / 2;
            res = CompareShortname(name, ((CFileInfo) curDir.longNameList.elementAt(mid)).shortname);
            if (res > 0) low = mid + 1; else if (res < 0) high = mid - 1; else {
                do {
                    foundNr = ((CFileInfo) curDir.longNameList.elementAt(mid)).shortNr;
                    mid++;
                } while (mid < curDir.longNameList.size() && (CompareShortname(name, ((CFileInfo) curDir.longNameList.elementAt(mid)).shortname) == 0));
                break;
            }
        }
        return foundNr + 1;
    }
