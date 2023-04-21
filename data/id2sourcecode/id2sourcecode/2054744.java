    void writeAllVisited() throws IOException {
        while (!nextRound.isEmpty()) {
            ArrayList<Integer> vec = nextRound;
            nextRound = new ArrayList<Integer>();
            for (int k = 0; k < vec.size(); ++k) {
                Integer i = vec.get(k);
                if (!visited.contains(i)) {
                    visited.add(i);
                    int n = i.intValue();
                    writer.addToBody(reader.getPdfObjectRelease(n), myXref[n]);
                }
            }
        }
    }
