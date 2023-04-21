    public String toString(int level) {
        StringBuffer readout = new StringBuffer();
        readout.append("\nNotebook Object:\n");
        for (Enumeration elements = new NObFieldEnumeration(keys()); elements.hasMoreElements(); ) {
            String nextKey = (String) elements.nextElement();
            if ((nextKey.equals("data")) && ((level == NOb.COMPLETEDEBUG) || (level == NOb.NONSIGNATUREONLY))) {
                writeData(readout);
            } else if (nextKey.startsWith("sig")) {
                if (level != NOb.NONSIGNATUREONLY) {
                    readout.append(nextKey + ": " + get(nextKey).toString() + "\n");
                }
            } else {
                if (level != NOb.SIGNATURESONLY) {
                    readout.append(nextKey + ": " + get(nextKey).toString() + "\n");
                }
            }
        }
        readout.append("End Notebook Object.\n\n");
        return readout.toString();
    }
