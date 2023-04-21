    private int Find(long targethash, int left, int right) {
        int returnvalue;
        if ((right - left) <= 1) {
            if (list.length == 0) returnvalue = 0; else if (list[left].hashvalue == targethash) returnvalue = left; else returnvalue = right;
        } else {
            int mid = (left + right) / 2;
            if (list[mid].hashvalue > targethash) returnvalue = Find(targethash, left, mid); else returnvalue = Find(targethash, mid, right);
        }
        return returnvalue;
    }
