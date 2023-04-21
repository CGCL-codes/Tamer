    public String getExplanation(String word) {
        int i = 0;
        int max = getWordNum();
        String w = "";
        int mid = 0;
        Location l = new Location();
        String exp = null;
        while (i <= max) {
            mid = (i + max) / 2;
            w = getWord(mid, l);
            if (w.compareTo(word) > 0) {
                max = mid - 1;
            } else if (w.compareTo(word) < 0) {
                i = mid + 1;
            } else {
                break;
            }
        }
        byte[] buffer = new byte[l.size];
        this.dz.seek(l.offset);
        try {
            this.dz.read(buffer, l.size);
        } catch (Exception e) {
            last_error = e.toString();
            buffer = null;
            exp = e.toString();
        }
        try {
            if (buffer == null) {
                exp = "Error when reading data\n" + exp;
            } else {
                exp = new String(buffer, "UTF8");
            }
        } catch (Exception e) {
            last_error = e.toString();
            e.printStackTrace();
        }
        return w + "\n" + exp;
    }
