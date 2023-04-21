    private void readFrequencies() throws Exception {
        BitSet bsOK = new BitSet();
        int n0 = atomSetCollection.getCurrentAtomSetIndex() + 1;
        String[] tokens;
        boolean done = false;
        while (!done && readLine() != null && line.indexOf("DESCRIPTION") < 0 && line.indexOf("MASS-WEIGHTED") < 0) if (line.toUpperCase().indexOf("ROOT") >= 0) {
            discardLinesUntilNonBlank();
            tokens = getTokens();
            if (Float.isNaN(Parser.parseFloatStrict(tokens[tokens.length - 1]))) {
                discardLinesUntilNonBlank();
                tokens = getTokens();
            }
            int frequencyCount = tokens.length;
            readLine();
            int iAtom0 = atomSetCollection.getAtomCount();
            int atomCount = atomSetCollection.getLastAtomSetAtomCount();
            boolean[] ignore = new boolean[frequencyCount];
            for (int i = 0; i < frequencyCount; ++i) {
                ignore[i] = done || (done = Parser.parseFloatStrict(tokens[i]) < 1) || !doGetVibration(++vibrationNumber);
                if (ignore[i]) continue;
                bsOK.set(vibrationNumber - 1);
                atomSetCollection.cloneLastAtomSet();
            }
            fillFrequencyData(iAtom0, atomCount, atomCount, ignore, false, 0, 0, null);
        }
        String[][] info = new String[vibrationNumber][];
        if (line.indexOf("DESCRIPTION") < 0) discardLinesUntilContains("DESCRIPTION");
        while (discardLinesUntilContains("VIBRATION") != null) {
            tokens = getTokens();
            int freqNo = parseInt(tokens[1]);
            tokens[0] = getTokens(readLine())[1];
            if (tokens[2].equals("ATOM")) tokens[2] = null;
            info[freqNo - 1] = tokens;
            if (freqNo == vibrationNumber) break;
        }
        for (int i = vibrationNumber - 1; --i >= 0; ) if (info[i] == null) info[i] = info[i + 1];
        for (int i = 0, n = n0; i < vibrationNumber; i++) {
            if (!bsOK.get(i)) continue;
            atomSetCollection.setCurrentAtomSetIndex(n++);
            atomSetCollection.setAtomSetFrequency(null, info[i][2], info[i][0], null);
        }
    }
