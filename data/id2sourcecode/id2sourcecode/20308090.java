    public void removeSegment(int location) {
        numSegments--;
        tempTransformGroups = new TransformGroup[numSegments];
        temp = new int[numSegments];
        ChromosomeSegment newSegments[] = new ChromosomeSegment[numSegments];
        TransformGroup newTransformGroups[] = new TransformGroup[numSegments];
        int newPos[] = new int[numSegments];
        int index = locationToIndex(location);
        int deleted = currentPos[index];
        for (int i = 0; i < index; i++) newPos[i] = currentPos[i];
        for (int i = index; i < newPos.length; i++) newPos[i] = currentPos[i + 1];
        for (int i = 0; i < newPos.length; i++) if (newPos[i] > deleted) newPos[i]--;
        for (int i = 0; i < deleted; i++) {
            newSegments[i] = segments[i];
            newTransformGroups[i] = transformGroups[i];
        }
        for (int i = deleted; i < newSegments.length; i++) {
            newSegments[i] = segments[i + 1];
            newTransformGroups[i] = transformGroups[i + 1];
        }
        segments = newSegments;
        transformGroups = newTransformGroups;
        currentPos = newPos;
    }
