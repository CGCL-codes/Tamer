    @Override
    public double solve() {
        double r1 = (a + b) / 2;
        double r2 = smallFX.evaluate(r1);
        steps.add(r1);
        steps.add(r2);
        numberOfSteps++;
        while (Math.abs(r1 - r2) > accuracy) {
            numberOfSteps++;
            r1 = r2;
            r2 = getNext();
            steps.add(r2);
        }
        System.out.println(r2);
        System.out.println(numberOfSteps);
        return r2;
    }
