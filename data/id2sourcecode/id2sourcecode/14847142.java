    private LinkedList dichoFind(String code, long p1, long p2) throws IOException {
        long pm = (p1 + p2) / 2;
        dictFile.seek(pm);
        String l;
        if (encoding == null) l = dictFile.readLine(); else l = dictReadLine();
        pm = dictFile.getFilePointer();
        if (encoding == null) l = dictFile.readLine(); else l = dictReadLine();
        long pm2 = dictFile.getFilePointer();
        if (pm2 >= p2) return (seqFind(code, p1, p2));
        int istar = l.indexOf('*');
        if (istar == -1) throw new IOException("bad format: no * !");
        String testcode = l.substring(0, istar);
        int comp = code.compareTo(testcode);
        if (comp < 0) return (dichoFind(code, p1, pm - 1)); else if (comp > 0) return (dichoFind(code, pm2, p2)); else {
            LinkedList l1 = dichoFind(code, p1, pm - 1);
            LinkedList l2 = dichoFind(code, pm2, p2);
            String word = l.substring(istar + 1);
            l1.add(word);
            l1.addAll(l2);
            return (l1);
        }
    }
