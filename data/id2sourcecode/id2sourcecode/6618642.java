    public void animateTransposition(int start, int end, int insertion, float distance, int count, int max) {
        start = (int) (start * numSegments / length) % numSegments;
        end = (int) ((end * numSegments / length) + 1) % numSegments;
        insertion = (int) ((insertion * numSegments / length) + 1) % numSegments;
        if (start == end + 1) return;
        if (count == 0) {
            for (int i = 0; i < transformGroups.length; i++) {
                transformGroups[i].getTransform(tr);
                tempTransformGroups[i] = new TransformGroup(tr);
            }
            return;
        }
        Transform3D firstTranslation = new Transform3D();
        Transform3D secondTranslation = new Transform3D();
        Transform3D outerRotation = new Transform3D();
        double startAngle = 2 * Math.PI / numSegments * start;
        double endAngle = 2 * Math.PI / numSegments * end;
        double vectorAngle = (startAngle + endAngle) / 2;
        if (start > end) vectorAngle += Math.PI;
        int tempCount;
        if (count <= max / 3) tempCount = count; else tempCount = (int) (max / 3);
        int maxTempCount = max / 3;
        if (tempCount != 0) {
            firstTranslation.setTranslation(new Vector3f((float) (Math.sin(vectorAngle) * distance * tempCount / maxTempCount), (float) (Math.cos(vectorAngle) * distance * tempCount / maxTempCount), 0f));
        }
        if (count <= max / 3) tempCount = 0; else if (count < max * 2 / 3) tempCount = count - max / 3; else tempCount = max / 3;
        if (tempCount != 0) {
            double outerAngle = -Math.PI * 2 / numSegments * (insertion - end);
            outerRotation.setRotation(new AxisAngle4d(0, 0, 1, outerAngle * tempCount / maxTempCount));
            Transform3D innerRotation = new Transform3D();
            double innerAngle;
            if (start > end) innerAngle = Math.PI * 2 / numSegments * (end - start + numSegments); else innerAngle = Math.PI * 2 / numSegments * (end - start);
            innerRotation.setRotation(new AxisAngle4d(0, 0, 1, innerAngle * tempCount / maxTempCount));
            for (int i = end; i != insertion; i = (i + 1) % numSegments) {
                tempTransformGroups[currentPos[i]].getTransform(tr);
                tempTransform.mul(innerRotation, tr);
                transformGroups[currentPos[i]].setTransform(tempTransform);
            }
        }
        if (count <= max * 2 / 3) tempCount = 0; else tempCount = count - max * 2 / 3;
        if (tempCount != 0) {
            startAngle = 2 * Math.PI / numSegments * (insertion + start - end);
            endAngle = 2 * Math.PI / numSegments * insertion;
            vectorAngle = (startAngle + endAngle) / 2;
            if (start > end) vectorAngle += Math.PI;
            secondTranslation.setTranslation(new Vector3f((float) -(Math.sin(vectorAngle) * distance * tempCount / maxTempCount), (float) -(Math.cos(vectorAngle) * distance * tempCount / maxTempCount), 0f));
        }
        secondTranslation.mul(outerRotation);
        secondTranslation.mul(firstTranslation);
        for (int i = start; i != end; i = (i + 1) % numSegments) {
            tempTransformGroups[currentPos[i]].getTransform(tr);
            tempTransform.mul(secondTranslation, tr);
            transformGroups[currentPos[i]].setTransform(tempTransform);
        }
        if (count == max) transpose(start, end, insertion, currentPos);
    }
