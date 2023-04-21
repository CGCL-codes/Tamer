    public NamedPolicyEvaluator[] deleteEvals(NamedPolicyEvaluator[] orig_npe, NamedPolicyEvaluator[] policy_evaluator_list) {
        boolean present;
        int k, curr_length = orig_npe.length;
        for (int i = 0; i < policy_evaluator_list.length; i++) {
            present = false;
            for (int j = 0; j < curr_length; i++) {
                if (orig_npe[j].evaluator_name.equals(policy_evaluator_list[i].evaluator_name)) {
                    for (k = j; k < curr_length - 1; k++) {
                        orig_npe[k] = orig_npe[k + 1];
                    }
                    curr_length--;
                    present = true;
                }
            }
            if (!present) {
                return null;
            }
        }
        NamedPolicyEvaluator[] npe = new NamedPolicyEvaluator[curr_length];
        for (int i = 0; i < curr_length; i++) {
            npe[i] = orig_npe[i];
        }
        return npe;
    }
