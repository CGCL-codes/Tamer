    public static String findPattern(String name, String dir, String[] nameList) {
        if (dir == null) dir = ""; else if (!dir.equals("") && !dir.endsWith(File.separator)) {
            dir += File.separator;
        }
        int len = name.length();
        int bound = (len + 1) / 2;
        int[] indexList = new int[bound];
        int[] endList = new int[bound];
        int q = 0;
        boolean num = false;
        int ndx = -1, e = 0;
        for (int i = 0; i < len; i++) {
            char c = name.charAt(i);
            if (c >= '0' && c <= '9') {
                if (num) e++; else {
                    num = true;
                    ndx = i;
                    e = ndx + 1;
                }
            } else if (num) {
                num = false;
                indexList[q] = ndx;
                endList[q] = e;
                q++;
            }
        }
        if (num) {
            indexList[q] = ndx;
            endList[q] = e;
            q++;
        }
        StringBuffer sb = new StringBuffer(dir);
        for (int i = 0; i < q; i++) {
            int last = i > 0 ? endList[i - 1] : 0;
            sb.append(name.substring(last, indexList[i]));
            String pre = name.substring(0, indexList[i]);
            String post = name.substring(endList[i]);
            NumberFilter filter = new NumberFilter(pre, post);
            String[] list = matchFiles(nameList, filter);
            if (list == null || list.length == 0) return null;
            if (list.length == 1) {
                sb.append(name.substring(indexList[i], endList[i]));
                continue;
            }
            boolean fix = true;
            for (int j = 0; j < list.length; j++) {
                if (list[j].length() != len) {
                    fix = false;
                    break;
                }
            }
            if (fix) {
                int width = endList[i] - indexList[i];
                boolean[] same = new boolean[width];
                for (int j = 0; j < width; j++) {
                    same[j] = true;
                    int jx = indexList[i] + j;
                    char c = name.charAt(jx);
                    for (int k = 0; k < list.length; k++) {
                        if (list[k].charAt(jx) != c) {
                            same[j] = false;
                            break;
                        }
                    }
                }
                int j = 0;
                while (j < width) {
                    int jx = indexList[i] + j;
                    if (same[j]) {
                        sb.append(name.charAt(jx));
                        j++;
                    } else {
                        while (j < width && !same[j]) j++;
                        String p = findPattern(name, nameList, jx, indexList[i] + j, "");
                        if (p == null) {
                            return null;
                        }
                        sb.append(p);
                    }
                }
            } else {
                BigInteger[] numbers = new BigInteger[list.length];
                for (int j = 0; j < list.length; j++) {
                    numbers[j] = filter.getNumber(list[j]);
                }
                Arrays.sort(numbers);
                String bounds = getBounds(numbers, false);
                if (bounds == null) return null;
                sb.append(bounds);
            }
        }
        sb.append(q > 0 ? name.substring(endList[q - 1]) : name);
        return sb.toString();
    }
