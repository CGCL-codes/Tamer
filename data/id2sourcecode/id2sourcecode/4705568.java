    public static void main(String[] args) {
        try {
            File blastOutputFile = new File(args[0]);
            File tmpOriginal = new File(blastOutputFile.getAbsolutePath() + ".original");
            FileUtil.moveFileUsingSystemCall(blastOutputFile, tmpOriginal);
            Scanner scanner = new Scanner(tmpOriginal);
            FileWriter tmpWriter = new FileWriter(blastOutputFile);
            String currentQueryDefLine;
            String currentDeflineId;
            int offset = 0;
            long fullLength = 0;
            try {
                while (scanner.hasNextLine()) {
                    String nextLine = scanner.nextLine();
                    if (nextLine.indexOf(BLAST_OUTPUT_QUERY_DEF_OPEN) >= 0) {
                        String padding = getPadding(nextLine, BLAST_OUTPUT_QUERY_DEF_OPEN);
                        currentQueryDefLine = nextLine.trim();
                        currentQueryDefLine = currentQueryDefLine.substring(BLAST_OUTPUT_QUERY_DEF_OPEN.length(), currentQueryDefLine.length() - BLAST_OUTPUT_QUERY_DEF_CLOSE.length());
                        String[] defPieces = currentQueryDefLine.split("\\s+");
                        currentDeflineId = defPieces[0];
                        offset = Integer.valueOf(defPieces[1].trim().substring("/offset=".length()));
                        fullLength = Long.valueOf(defPieces[3].trim().substring("/full_length=".length()));
                        nextLine = padding + BLAST_OUTPUT_QUERY_DEF_OPEN + currentDeflineId + BLAST_OUTPUT_QUERY_DEF_CLOSE;
                    } else if (nextLine.indexOf(BLAST_OUTPUT_QUERY_LENGTH_OPEN) >= 0) {
                        String padding = getPadding(nextLine, BLAST_OUTPUT_QUERY_LENGTH_OPEN);
                        nextLine = padding + BLAST_OUTPUT_QUERY_LENGTH_OPEN + fullLength + BLAST_OUTPUT_QUERY_LENGTH_CLOSE;
                    } else if (nextLine.indexOf(ITERATION_QUERY_LENGTH_OPEN) >= 0) {
                        String padding = getPadding(nextLine, ITERATION_QUERY_LENGTH_OPEN);
                        nextLine = padding + ITERATION_QUERY_LENGTH_OPEN + fullLength + ITERATION_QUERY_LENGTH_CLOSE;
                    } else if (nextLine.indexOf(ITERATION_QUERY_DEF_OPEN) >= 0) {
                        String padding = getPadding(nextLine, ITERATION_QUERY_DEF_OPEN);
                        currentQueryDefLine = nextLine.trim();
                        currentQueryDefLine = currentQueryDefLine.substring(ITERATION_QUERY_DEF_OPEN.length(), currentQueryDefLine.length() - ITERATION_QUERY_DEF_CLOSE.length());
                        String[] defPieces = currentQueryDefLine.split("\\s+");
                        currentDeflineId = defPieces[0];
                        offset = Integer.valueOf(defPieces[1].trim().substring("/offset=".length()));
                        nextLine = padding + ITERATION_QUERY_DEF_OPEN + currentDeflineId + ITERATION_QUERY_DEF_CLOSE;
                    } else if (nextLine.indexOf(HSP_QUERY_FROM_OPEN) >= 0 && offset != 0) {
                        String padding = getPadding(nextLine, HSP_QUERY_FROM_OPEN);
                        String hspFrom = nextLine.trim();
                        long tmpLoc = Long.valueOf(hspFrom.substring(HSP_QUERY_FROM_OPEN.length(), hspFrom.length() - HSP_QUERY_FROM_CLOSE.length()));
                        tmpLoc += offset;
                        nextLine = padding + HSP_QUERY_FROM_OPEN + tmpLoc + HSP_QUERY_FROM_CLOSE;
                    } else if (nextLine.indexOf(HSP_QUERY_TO_OPEN) >= 0 && offset != 0) {
                        String padding = getPadding(nextLine, HSP_QUERY_TO_OPEN);
                        String hspTo = nextLine.trim();
                        long tmpLoc = Long.valueOf(hspTo.substring(HSP_QUERY_TO_OPEN.length(), hspTo.length() - HSP_QUERY_TO_CLOSE.length()));
                        tmpLoc += offset;
                        nextLine = padding + HSP_QUERY_TO_OPEN + tmpLoc + HSP_QUERY_TO_CLOSE;
                    } else if (nextLine.indexOf(ITERATION_CLOSE) >= 0) {
                        offset = 0;
                    }
                    tmpWriter.write(nextLine + "\n");
                }
            } finally {
                scanner.close();
                tmpWriter.flush();
                tmpWriter.close();
            }
            boolean deleted = tmpOriginal.delete();
            if (!deleted) {
                System.out.println("File " + tmpOriginal.getName() + " could not be deleted.");
            }
            File oosFile = new File(blastOutputFile.getAbsolutePath() + ".oos");
            if (oosFile.exists()) {
                boolean oosDeleted = oosFile.delete();
                if (!oosDeleted) {
                    System.out.println("File " + oosFile.getName() + " could not be deleted.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
