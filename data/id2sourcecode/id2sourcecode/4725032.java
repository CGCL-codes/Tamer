    void writeAllVisited() throws IOException {
        while (!nextRound.isEmpty()) {
            ArrayList vec = nextRound;
            nextRound = new ArrayList();
            for (int k = 0; k < vec.size(); ++k) {
                Integer i = (Integer) vec.get(k);
                if (!visited.containsKey(i)) {
                    visited.put(i, null);
                    int n = i.intValue();
                    writer.addToBody(reader.getPdfObjectRelease(n), myXref[n]);
                }
            }
        }
    }
