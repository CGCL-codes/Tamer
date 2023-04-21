    public void getBasicnumber(int numberofClasses, int maxlengthRecommended) {
        if (numberofClasses <= maxlengthRecommended) {
        } else {
            if ((numberofClasses % 2) == 1) {
                numberofClasses = (numberofClasses + 1) / 2;
                this.getBasicnumber(numberofClasses, maxlengthRecommended);
            } else {
                numberofClasses = (numberofClasses + 2) / 2;
                this.getBasicnumber(numberofClasses, maxlengthRecommended);
            }
        }
    }
