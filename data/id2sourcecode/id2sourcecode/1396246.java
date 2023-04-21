    public void removeCustomizedSize(int cellIndex) {
        int s = 0;
        int e = _customizedSize.length;
        if (e == 0) {
            return;
        }
        int i;
        while (s < e) {
            i = s + (e - s) / 2;
            if (_customizedSize[i][0] == cellIndex) {
                remove(i);
                return;
            } else if (_customizedSize[i][0] > cellIndex) {
                e = i - 1;
            } else {
                s = i + 1;
            }
            if (e == s) {
                if (e < _customizedSize.length && _customizedSize[e][0] == cellIndex) {
                    remove(e);
                }
                break;
            } else if (e < s) {
                break;
            }
        }
    }
