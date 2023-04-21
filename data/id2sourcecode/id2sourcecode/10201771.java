    @Override
    public final List<BookEntry> getBookEntries(Position pos) {
        try {
            RandomAccessFile f = new RandomAccessFile(bookFile, "r");
            long numEntries = f.length() / 16;
            long key = getHashKey(pos);
            PGBookEntry ent = new PGBookEntry();
            long lo = -1;
            long hi = numEntries;
            while (hi - lo > 1) {
                long mid = (lo + hi) / 2;
                readEntry(f, mid, ent);
                long midKey = ent.getKey();
                if (keyLess(midKey, key)) {
                    lo = mid;
                } else {
                    hi = mid;
                }
            }
            List<BookEntry> ret = new ArrayList<BookEntry>();
            long entNo = hi;
            while (entNo < numEntries) {
                readEntry(f, entNo, ent);
                if (ent.getKey() != key) break;
                Move m = ent.getMove(pos);
                BookEntry be = new BookEntry(m);
                be.weight = ent.getWeight();
                ret.add(be);
                entNo++;
            }
            f.close();
            return ret;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }
