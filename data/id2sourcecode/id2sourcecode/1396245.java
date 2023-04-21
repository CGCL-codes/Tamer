    public void setCustomizedSize(int cellIndex, int size, int id) {
        int s = 0;
        int e = _customizedSize.length;
        if (e == 0) {
            insert(0, cellIndex, size, id);
            return;
        }
        int i;
        while (s < e) {
            i = s + (e - s) / 2;
            if (_customizedSize[i][0] == cellIndex) {
                _customizedSize[i][1] = size;
                _customizedSize[i][2] = id;
                return;
            } else if (_customizedSize[i][0] > cellIndex) {
                e = i - 1;
            } else {
                s = i + 1;
            }
            if (e == s) {
                if (e >= _customizedSize.length || _customizedSize[e][0] > cellIndex) {
                    insert(e, cellIndex, size, id);
                } else if (_customizedSize[e][0] == cellIndex) {
                    _customizedSize[e][1] = size;
                    _customizedSize[e][2] = id;
                } else {
                    insert(e + 1, cellIndex, size, id);
                }
                break;
            } else if (e < s) {
                insert(s, cellIndex, size, id);
            }
        }
    }
