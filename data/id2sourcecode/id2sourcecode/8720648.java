    void scaleModel(float eventDistance) {
        if (!scalingEnabled) return;
        if (eventDistance > irisRadius && eventDistance < irisRadius + rimThickness) {
            irisRadius = scaleRadius(eventDistance, irisRadius);
            if (irisRadius > maxIris) irisRadius = maxIris;
            computeModelArea();
            return;
        }
        if (eventDistance < pupilRadius) {
            pupilRadius = scaleRadius(eventDistance, pupilRadius);
            return;
        }
        float avg = (irisRadius + pupilRadius) / 2;
        if (eventDistance > avg) {
            irisRadius = scaleRadius(eventDistance, irisRadius);
            computeModelArea();
        } else {
            pupilRadius = scaleRadius(eventDistance, pupilRadius);
            if (pupilRadius > maxPupil) pupilRadius = maxPupil;
        }
    }
