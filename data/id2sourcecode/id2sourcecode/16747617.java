    protected float[][] computeCM(List<String> targetNodeNameList, List<String> evidenceNodeNameList) throws EvaluationException {
        init(targetNodeNameList, evidenceNodeNameList);
        targetNode = targetNodeList[0];
        if (targetNodeList.length != 1) {
            throw new EvaluationException("For now, just one target node is accepted!");
        }
        float[][] postTGivenE = new float[targetNode.getStatesSize()][evidenceStatesProduct];
        float[][] postEGivenT = new float[evidenceStatesProduct][targetNode.getStatesSize()];
        for (int row = 0; row < statesProduct; row++) {
            int[] states = getMultidimensionalCoord(row);
            int indexTarget = states[0];
            int indexEvidence = getEvidenceLinearCoord(states);
            float probTGivenE = getProbTargetGivenEvidence(states);
            postTGivenE[indexTarget][indexEvidence] = probTGivenE;
            int[] evidencesStates = new int[states.length - 1];
            for (int i = 0; i < evidencesStates.length; i++) {
                evidencesStates[i] = states[i + 1];
            }
            float probE = getEvidencesJointProbability(evidencesStates);
            float probT = getTargetPriorProbability(states[0]);
            float probEGivenT = probTGivenE * probE / probT;
            postEGivenT[indexEvidence][indexTarget] = probEGivenT;
        }
        int N = targetNode.getStatesSize();
        float[][] CM = new float[N][N];
        for (int i = 0; i < N; i++) {
            float[] arowi = postTGivenE[i];
            float[] crowi = CM[i];
            for (int k = 0; k < evidenceStatesProduct; k++) {
                float[] browk = postEGivenT[k];
                float aik = arowi[k];
                for (int j = 0; j < N; j++) {
                    crowi[j] += aik * browk[j];
                }
            }
        }
        return CM;
    }
