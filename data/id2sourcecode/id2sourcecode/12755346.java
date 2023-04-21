    private void fetchNextMappedRead() {
        try {
            readID = Util.getIthField(nextLine, 9);
            bestScore = Integer.parseInt(Util.getIthField(nextLine, 0));
            readLen = Integer.parseInt(Util.getIthField(nextLine, 10));
            nextMappedReadRecords = new ArrayList();
            nextMappedReadRecords.add(getAlignmentRecord(nextLine));
            while (fetchNextLineStartWithNumber() != null) {
                String line = nextLine;
                String nextReadID = Util.getIthField(line, 9);
                if (nextReadID.equals(readID)) {
                    nextMappedReadRecords.add(getAlignmentRecord(line));
                    int score = Integer.parseInt(Util.getIthField(nextLine, 0));
                    if (score > bestScore) bestScore = score;
                } else {
                    break;
                }
            }
        } catch (Exception ex) {
            System.err.println("LINE: " + nextLine);
            ex.printStackTrace();
            System.exit(1);
        }
    }
