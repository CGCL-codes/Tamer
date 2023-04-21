    public void calculateMovement(double timestepS) {
        weightOnFrontWheelsN = carDefinition.getEmptyMassKg() * 9.81 / 2;
        weightOnRearWheelsN = carDefinition.getEmptyMassKg() * 9.81 / 2;
        maxForceToGroundN = weightOnRearWheelsN * 1.1;
        engineTorqueNm = carDefinition.getEngineMaxTorqueNm() * getThrottle();
        wheelTorqueNm = engineTorqueNm * carDefinition.getGearRatio(getGear()) * carDefinition.getDifferentialRatio();
        wheelForceN = wheelTorqueNm / (carDefinition.getDiameterRearWheelM() / 2);
        freeRollingWheelAngularVelocity_s = velocityM_s / carDefinition.diameterRearWheelM * 2;
        wheelAngularVelocity_s = freeRollingWheelAngularVelocity_s + 0.5;
        engineRPM_s = wheelAngularVelocity_s * carDefinition.getGearRatio(getGear()) * carDefinition.getDifferentialRatio();
        if (wheelForceN > maxForceToGroundN - 1) {
            longitudinalSlip = 8;
        } else {
            double abweichung = 2;
            double left = 0;
            double right = 100;
            longitudinalSlip = 0.4;
            int turns = 0;
            do {
                double gripForceX = calcGripForceN(longitudinalSlip, maxForceToGroundN);
                abweichung = gripForceX / wheelForceN;
                if (abweichung < 1) {
                    left = longitudinalSlip;
                    longitudinalSlip = (longitudinalSlip + right) / 2;
                } else if (abweichung > 1) {
                    right = longitudinalSlip;
                    longitudinalSlip = (longitudinalSlip + left) / 2;
                }
                turns++;
            } while (Math.abs(abweichung - 1) > 0.01);
        }
        gripForceN = calcGripForceN(longitudinalSlip, maxForceToGroundN);
        rollingResistanceN = 0.7 * velocityM_s;
        airDragN = (carDefinition.getCoefficientOfDrag() * carDefinition.getFrontalAreaMm() * AIRDENSITYG_L * velocityM_s * velocityM_s) * 0.5;
        nettoForceN = gripForceN - rollingResistanceN - airDragN;
        accelerationM_ss = nettoForceN / getMass();
        velocityM_s = velocityM_s + timestepS * accelerationM_ss;
        position.add(direction, velocityM_s * timestepS);
        if (dataServer != null) dataServer.dataUpdate(dataTypeVelocityId, new Double(velocityM_s));
    }
