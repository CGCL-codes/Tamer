    public void getSteps(int numberofClasses, int[] steps, int maxlengthRecommended) {
        if (numberofClasses <= maxlengthRecommended) {
        } else {
            if ((numberofClasses % 2) == 1) {
                numberofClasses = (numberofClasses + 1) / 2;
                this.currentRound = this.currentRound + 1;
                steps[this.currentRound] = numberofClasses;
                this.getSteps(numberofClasses, steps, maxlengthRecommended);
            } else {
                numberofClasses = (numberofClasses + 2) / 2;
                this.currentRound = this.currentRound + 1;
                steps[this.currentRound] = numberofClasses;
                this.getSteps(numberofClasses, steps, maxlengthRecommended);
            }
        }
    }
