    public double[] predict(WordType wordType) {
        if (maxOrder == 1) {
            double[] res = new double[WordType.vocabularySize()];
            double sum = 0;
            for (int i = 0; i < WordType.vocabularySize(); i++) {
                String pred = WordType.values()[i].getOriginalTag();
                res[i] = Math.exp(unigram.get(pred).getProb());
                sum += res[i];
            }
            for (int i = 0; i < WordType.vocabularySize(); i++) {
                res[i] /= sum;
            }
            return res;
        }
        if (history == null) {
            history = new String[maxOrder - 1];
        }
        if (wordType == WordType.PERIOD || wordType == null) {
            history[0] = "<s>";
            historyLen = 1;
        } else {
            for (int i = 0; i < historyLen - 1; i++) {
                history[i] = history[i + 1];
            }
            if (historyLen == history.length) {
                history[historyLen - 1] = wordType.getOriginalTag();
            } else {
                history[historyLen] = wordType.getOriginalTag();
                historyLen++;
            }
        }
        double[] res = predictAll(history, historyLen);
        return res;
    }
