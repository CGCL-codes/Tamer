    public static int getFreq(String word) {
        Integer cnto = (Integer) word2cnt.get(word);
        if (cnto == null) {
            cnto = MAXINT;
            try {
                URL url = new URL(Engine + word);
                Reader in = new BufferedReader(new InputStreamReader(url.openStream()));
                char[] buf = new char[HUNK_LEN];
                int len = 0, hunk;
                while (len < HUNK_LEN && (hunk = in.read(buf, len, buf.length - len)) > 0) len += hunk;
                in.close();
                String result = new String(buf, 0, len);
                int inx0 = result.indexOf(EngineHook), imax = Math.min(inx0 + 200, result.length());
                if (inx0 != -1) {
                    char ch;
                    boolean skip = false;
                    int num = 0, inx = inx0;
                    for (; inx < imax && (!Character.isDigit((ch = result.charAt(inx))) || skip); inx++) {
                        if (ch == '<') skip = true; else if (skip && ch == '>') skip = false;
                    }
                    if (inx == imax) {
                        inx = inx0;
                        skip = false;
                        int imin = Math.max(0, inx0 - 200);
                        for (; inx > imin && (!Character.isDigit((ch = result.charAt(inx))) || skip); inx--) {
                            if (ch == '<') skip = true; else if (skip && ch == '>') skip = false;
                        }
                        imax = inx + 1;
                        while (inx > imin && (Character.isDigit(ch = result.charAt(inx)) || ch == ',')) inx--;
                        inx++;
                    }
                    for (int inx2 = inx; inx2 < imax; inx2++) {
                        ch = result.charAt(inx2);
                        if (Character.isDigit(ch)) num = num * 10 + (((int) ch) - '0'); else if (ch == ',') {
                        } else break;
                    }
                    if (DEBUG) System.out.print("new word " + word + " => " + num); else if (Verbose) {
                        if (firstNew) {
                            System.out.print("Fetching words not in caches.");
                            firstNew = false;
                        }
                        System.out.print(".");
                    }
                    if (num == 0) cnto = MAXINT; else if (num < MEDFREQ) cnto = LILINTS[num / LILROUND]; else if (num < BIGFREQ) cnto = MEDINTS[(num - MEDFREQ) / MEDROUND]; else cnto = MAXINT;
                    if (DEBUG) System.out.println("->" + cnto);
                }
            } catch (Exception e) {
                System.err.println(e);
            }
            word2cnt.put(word, cnto);
            newwords.add(word);
            if (newwords.size() > 50) writeCache();
        }
        return cnto.intValue();
    }
