    public void animateInversion(int orStart, int orEnd, int count, int max) {
        int start = locationToIndex(orStart);
        int end = locationToIndex(orEnd) + 1;
        if (start == end + 1) animateInversion(orEnd, orStart, count, max);
        if (count == 0) {
            for (int i = start; i != end; i = (i + 1) % numSegments) {
                transformGroups[currentPos[i]].getTransform(tr);
                tempTransformGroups[currentPos[i]] = new TransformGroup(tr);
            }
        }
        if (start != end + 1 && count >= 0 && count <= max) {
            double startAngle = 2 * Math.PI * segments[currentPos[start]].position / (double) length;
            double endAngle = 2 * Math.PI * segments[currentPos[end]].position / (double) length;
            double vectorAngle = (startAngle + endAngle) / 2;
            if (count == 0) System.out.println("Inverting from " + orStart + " ( " + start + ", " + startAngle + " ) to " + orEnd + " ( " + end + ", " + endAngle + " ) -> " + vectorAngle + " ( " + length + " )");
            Transform3D inversionTransform = new Transform3D();
            inversionTransform.setRotation(new AxisAngle4d(Math.sin(vectorAngle), Math.cos(vectorAngle), 0, -Math.PI * count / max));
            for (int i = start; i != end; i = (i + 1) % numSegments) {
                tempTransformGroups[currentPos[i]].getTransform(tr);
                tempTransform.mul(inversionTransform, tr);
                transformGroups[currentPos[i]].setTransform(tempTransform);
            }
        }
        if (start != end + 1 && count == max) invert(start, end, currentPos, false);
    }
