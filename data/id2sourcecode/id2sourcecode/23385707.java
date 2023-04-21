    public void printCandidatesOn(PrintWriter pw) {
        pw.println("check for potential read/write races on same fields");
        if (candidates.size() == 0) {
            pw.println("no test candidates found");
        } else {
            pw.println("number of potential test candidates: " + candidates.size());
            int i = 1;
            for (RaceCandidate candidate : candidates) {
                pw.println();
                pw.println("--- test #" + i++);
                candidate.printOn(pw);
            }
        }
    }
