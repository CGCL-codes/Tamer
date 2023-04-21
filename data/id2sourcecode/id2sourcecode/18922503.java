    public synchronized void generateSphere(int totalSteps) {
        this.clear();
        int thetaSteps = (int) (Math.PI / Math.sqrt(2 * Math.PI / totalSteps));
        double thetaDiff = Math.PI / thetaSteps;
        this.minRestFrameEnergy = Double.POSITIVE_INFINITY;
        this.maxRestFrameEnergy = Double.NEGATIVE_INFINITY;
        double averageEnergy = 0;
        double upperScatterAngle = 0;
        double upperSigma = PhysicsFunctions.angularCrossSectionUm(normalizedPhotonEnergy, upperScatterAngle);
        for (int i = 0; i < thetaSteps; i++) {
            double lowerScatterAngle = upperScatterAngle;
            double lowerSigma = upperSigma;
            upperScatterAngle = (i + 1) * Math.PI / (thetaSteps);
            upperSigma = PhysicsFunctions.angularCrossSectionUm(normalizedPhotonEnergy, upperScatterAngle);
            double averageScatterAngle = (lowerScatterAngle + upperScatterAngle) / 2;
            double sigmaSection = 0.5 * (lowerSigma + upperSigma) * (Math.PI / thetaSteps) * Math.sin(averageScatterAngle);
            this.integralCrossSection += sigmaSection;
            int phiSteps = (int) Math.round(totalSteps * 0.5 * Math.sin((lowerScatterAngle + upperScatterAngle) / 2) * thetaDiff);
            if (phiSteps < 1) phiSteps = 1;
            double proportion = (sigmaSection / phiSteps) / totalCrossSection;
            double upperPhiAngle = 0;
            for (int j = 0; j < phiSteps; j++) {
                double lowerPhiAngle = upperPhiAngle;
                upperPhiAngle = 2 * (j + 1) * Math.PI / (phiSteps);
                Sphericule section = new Sphericule(lowerScatterAngle, upperScatterAngle, lowerPhiAngle, upperPhiAngle, proportion);
                double rfEng = section.getPhotonEnergyRF();
                if (this.minRestFrameEnergy > rfEng) this.minRestFrameEnergy = rfEng;
                if (this.maxRestFrameEnergy < rfEng) this.maxRestFrameEnergy = rfEng;
                averageEnergy += proportion * section.getPhotonEnergyRF();
                this.sphericules.add(section);
            }
        }
        double checkSum = 0;
        for (Sphericule check : this.sphericules) checkSum += check.getProportion();
        Debug.INFO.print("Checksum on sphere: " + checkSum);
        Debug.INFO.print("Min RF energy: " + this.minRestFrameEnergy);
        Debug.INFO.print("Max RF energy: " + this.maxRestFrameEnergy);
        Debug.INFO.print("Average RF energy: " + averageEnergy);
        this.averageRestFrameEnergy = averageEnergy;
    }
