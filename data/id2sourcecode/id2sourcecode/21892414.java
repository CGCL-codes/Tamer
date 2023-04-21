    boolean GetShortName(String fullname, StringRef shortname) {
        StringRef expand = new StringRef();
        CFileInfo curDir = FindDirInfo(fullname, expand);
        int filelist_size = curDir.longNameList.size();
        if (filelist_size <= 0) return false;
        int low = 0;
        int high = filelist_size - 1;
        int mid, res;
        while (low <= high) {
            mid = (low + high) / 2;
            res = fullname.compareTo(((CFileInfo) curDir.longNameList.elementAt(mid)).orgname);
            if (res > 0) low = mid + 1; else if (res < 0) high = mid - 1; else {
                shortname.value = ((CFileInfo) curDir.longNameList.elementAt(mid)).shortname;
                return true;
            }
        }
        return false;
    }
