        public Sphericule(double minTheta, double maxTheta, double minPhi, double maxPhi, double propSigma) {
            this.minTheta = minTheta;
            this.maxTheta = maxTheta;
            this.minPhi = minPhi;
            this.maxPhi = maxPhi;
            this.averageTheta = (minTheta + maxTheta) / 2.0;
            this.averagePhi = (minPhi + maxPhi) / 2.0;
            this.propSigma = propSigma;
            Vector3D scatterFrame = new Vector3D(Math.sin(averageTheta) * Math.cos(averagePhi), Math.sin(averageTheta) * Math.sin(averagePhi), Math.cos(averageTheta));
            this.pointerRF = scatterFrame.transform(scatteringRotation[0][0], scatteringRotation[0][1], scatteringRotation[0][2], scatteringRotation[1][0], scatteringRotation[1][1], scatteringRotation[1][2], scatteringRotation[2][0], scatteringRotation[2][1], scatteringRotation[2][2]);
            this.pointerAFR = frameBooster.boostIntoAbsoluteFrame(this.pointerRF);
            double minThetaEng = photonEnergyRF * PhysicsFunctions.getComptonEnergyRatioRMF(normalizedPhotonEnergy, minTheta);
            double maxThetaEng = photonEnergyRF * PhysicsFunctions.getComptonEnergyRatioRMF(normalizedPhotonEnergy, maxTheta);
            this.scatterEnergyRF = (minThetaEng + maxThetaEng) / 2;
            Vector3D[] corners = new Vector3D[4];
            corners[0] = new Vector3D(Math.sin(minTheta) * Math.cos(minPhi), Math.sin(minTheta) * Math.sin(minPhi), Math.cos(minTheta));
            corners[1] = new Vector3D(Math.sin(minTheta) * Math.cos(maxPhi), Math.sin(minTheta) * Math.sin(maxPhi), Math.cos(minTheta));
            corners[2] = new Vector3D(Math.sin(maxTheta) * Math.cos(minPhi), Math.sin(maxTheta) * Math.sin(minPhi), Math.cos(maxTheta));
            corners[3] = new Vector3D(Math.sin(maxTheta) * Math.cos(maxPhi), Math.sin(maxTheta) * Math.sin(maxPhi), Math.cos(maxTheta));
            this.cornersRF = new Vector3D[4];
            for (int i = 0; i < 4; i++) cornersRF[i] = corners[i].transform(scatteringRotation[0][0], scatteringRotation[0][1], scatteringRotation[0][2], scatteringRotation[1][0], scatteringRotation[1][1], scatteringRotation[1][2], scatteringRotation[2][0], scatteringRotation[2][1], scatteringRotation[2][2]);
            this.cornersARF = new Vector3D[4];
            for (int i = 0; i < 4; i++) cornersARF[i] = frameBooster.boostIntoAbsoluteFrame(cornersRF[i]);
            this.cornerEnergies = new double[4];
            cornerEnergies[0] = frameBooster.getEnergyAFR(minThetaEng, cornersRF[0].getZ());
            cornerEnergies[1] = frameBooster.getEnergyAFR(minThetaEng, cornersRF[1].getZ());
            cornerEnergies[2] = frameBooster.getEnergyAFR(maxThetaEng, cornersRF[2].getZ());
            cornerEnergies[3] = frameBooster.getEnergyAFR(maxThetaEng, cornersRF[3].getZ());
            double minEng = Double.POSITIVE_INFINITY;
            double maxEng = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < 4; i++) {
                if (cornerEnergies[i] > maxEng) maxEng = cornerEnergies[i];
                if (cornerEnergies[i] < minEng) minEng = cornerEnergies[i];
            }
            this.minEnergy = minEng;
            this.maxEnergy = maxEng;
            this.scatterEnergyAFR = (minEng + maxEng) / 2.0;
        }
