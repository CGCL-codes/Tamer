    @Override
    public void setup(int stage, int screen) {
        switch(stage) {
            case EXPERIMENT_STAGE:
                if (screen == 0) {
                    int N = subjectsData.getNumberOfSubjects();
                    subjectsData.storeData(FIRST_MOVER_RESPONSE_DATA, new int[N]);
                    subjectsData.storeData(SECOND_MOVER_RESPONSE_DATA, new int[N][NUM_OF_CONDITIONAL_BOXES]);
                }
                if (screen == 3) {
                    BijectionPairing pairing = new BijectionPairing(subjectsData.getNumberOfSubjects());
                    subjectsData.storeData(PAIRING_FOR_STAGE + EXPERIMENT_STAGE, pairing);
                    int[] firstMoverTransfers = (int[]) subjectsData.getStoredData(FIRST_MOVER_RESPONSE_DATA);
                    int[][] secondMoverTransferSchemes = (int[][]) subjectsData.getStoredData(SECOND_MOVER_RESPONSE_DATA);
                    for (int subjectIndex = 0; subjectIndex < subjectsData.getNumberOfSubjects(); subjectIndex++) {
                        Subject subject = subjectsData.getSubjectByIDNumber(subjectIndex);
                        int pairedResponder = pairing.getRightPairing(subjectIndex);
                        int transferToSecondMover = firstMoverTransfers[subjectIndex];
                        int transferFromSecondMover = secondMoverTransferSchemes[pairedResponder][transferToSecondMover / TRANSFER_UNITS];
                        int payoffAsFirstMover = INITIAL_ENDOWMENT - transferToSecondMover + 2 * transferFromSecondMover;
                        subject.addPayment(payoffAsFirstMover);
                        int pairedFirstMover = pairing.getLeftPairing(subjectIndex);
                        int transferFromFirstMover = firstMoverTransfers[pairedFirstMover];
                        int transferToFirstMover = secondMoverTransferSchemes[subjectIndex][transferFromFirstMover / TRANSFER_UNITS];
                        int payoffAsResponder = INITIAL_ENDOWMENT + 2 * transferFromFirstMover - transferToFirstMover;
                        subject.addPayment(payoffAsResponder);
                    }
                }
        }
    }
