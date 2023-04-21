    private void dfsProcess(char[] str, int l, int r, Vector<String> vs) {
        String[] value = this.verbAbbrMap.get(String.copyValueOf(str, l, r - l + 1).toLowerCase());
        if (value != null) {
            for (int i = 0; i < value.length; i++) {
                if (value[i].length() > 0) vs.add(value[i]);
            }
            return;
        }
        int id = l;
        while (true) {
            while (id <= r && !isMark[str[id]]) id++;
            if (id > r) {
                break;
            }
            if (!isMark[str[id + 1]]) {
                id++;
                continue;
            }
            dfsProcess(str, l, id - 1, vs);
            while (isMark[str[id]]) id++;
            l = id;
        }
        for (int i = r - 1; i > l; i--) {
            if (str[i] != '.' && str[i] != ',') continue;
            if (Character.isDigit(str[i - 1]) && Character.isDigit(str[i + 1])) {
                for (int j = i; j < r; j++) str[j] = str[j + 1];
                r--;
            }
        }
        int len = r - l + 1;
        while (len > 2 && (str[r - 1] == '\'' || str[r - 1] == '"') && (str[r] == 's' || str[r] == 'S' || str[r] == 't' || str[r] == 'd')) {
            r -= 2;
            len -= 2;
        }
        while (len > 3 && (str[r - 2] == '\'' || str[r - 2] == '"') && str[r - 1] == 'l' && str[r] == 'l') {
            r -= 3;
            len -= 3;
        }
        int letterOrDigitRequired = 3;
        id = l + 1;
        while (true) {
            for (; id < r && str[id] != '-'; id++) ;
            if (id >= r) break;
            int isLetter = 2;
            for (int i = id - 1; i >= id - letterOrDigitRequired; i--) {
                if (i < l || !Character.isLetterOrDigit(str[i])) {
                    isLetter--;
                    break;
                }
            }
            for (int i = id + 1; i <= id + letterOrDigitRequired; i++) {
                if (i > r || !Character.isLetterOrDigit(str[i])) {
                    isLetter--;
                    break;
                }
            }
            if (isLetter > 0) {
                dfsProcess(str, l, id - 1, vs);
                l = id + 1;
            }
            id += 1;
        }
        letterOrDigitRequired = 3;
        id = l + 1;
        while (true) {
            for (; id < r && str[id] != '.' && str[id] != ':' && str[id] != '\''; id++) ;
            if (id >= r) break;
            boolean isLetter = true;
            for (int i = id - 1; i >= id - letterOrDigitRequired; i--) {
                if (i < l || !Character.isLetterOrDigit(str[i])) {
                    isLetter = false;
                    break;
                }
            }
            for (int i = id + 1; i <= id + letterOrDigitRequired; i++) {
                if (i > r || !Character.isLetterOrDigit(str[i])) {
                    isLetter = false;
                    break;
                }
            }
            if (isLetter) {
                dfsProcess(str, l, id - 1, vs);
                l = id + 1;
            }
            id += 1;
        }
        if (this.timeToConst) {
            if (r - l + 1 == 5) {
                if (str[l + 2] == ':' && Character.isDigit(str[l]) && Character.isDigit(str[l + 1]) && Character.isDigit(str[l + 3]) && Character.isDigit(str[l + 4])) {
                    boolean valid = true;
                    if (str[l] > '2') valid = false;
                    if (str[l] == '2' && str[l + 1] > '4') valid = false;
                    if (str[l + 3] > '5') valid = false;
                    if (valid) {
                        vs.add(Constant.TIME_FEATURE);
                        return;
                    }
                }
            }
        }
        len = r - l + 1;
        id = l;
        while (true) {
            for (; id <= r && !this.isBadMark[str[id]]; id++) ;
            if (id > r) break;
            if (str[id] == '/' && len <= 4) {
                id++;
                continue;
            }
            dfsProcess(str, l, id - 1, vs);
            id = l = id + 1;
            len = r - l + 1;
        }
        if (this.numToConst) {
            for (id = l; id <= r; id++) {
                if (!Character.isDigit(str[id]) && !this.isMark[str[id]]) break;
            }
            if (id > r) {
                vs.add(Constant.NUMBER_FEATURE);
                return;
            }
        }
        vs.add(String.copyValueOf(str, l, r - l + 1));
    }
