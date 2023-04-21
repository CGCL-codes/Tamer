    public void getNumberofround(int numberofClasses, int maxlengthRecommended) {
        if (numberofClasses <= maxlengthRecommended) {
            this.numberofRound = this.numberofRound + 0;
        } else {
            if ((numberofClasses % 2) == 1) {
                numberofClasses = (numberofClasses + 1) / 2;
                this.numberofRound = this.numberofRound + 1;
                this.getNumberofround(numberofClasses, maxlengthRecommended);
            } else {
                numberofClasses = (numberofClasses + 2) / 2;
                this.numberofRound = this.numberofRound + 1;
                this.getNumberofround(numberofClasses, maxlengthRecommended);
            }
        }
    }
