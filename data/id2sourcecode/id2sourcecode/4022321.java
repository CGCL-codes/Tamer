    protected Vector3f computeNewLeafTranslation(List<TreeLeaf3D> leaves3D, float branchLength) {
        float previousAttachPoint = 0;
        float saveAttachPoint1 = -1;
        float saveAttachPoint2 = -1;
        float distance;
        float maxDistance = -1;
        List<TreeLeaf3D> sortedLeaves3D = new ArrayList<TreeLeaf3D>(leaves3D);
        Collections.sort(sortedLeaves3D, new TreeLeaf3DComparator());
        for (TreeLeaf3D leaf3D : sortedLeaves3D) {
            float attachPoint = leaf3D.getPosition().getY();
            distance = attachPoint - previousAttachPoint;
            if (distance > maxDistance) {
                maxDistance = distance;
                saveAttachPoint1 = previousAttachPoint;
                saveAttachPoint2 = attachPoint;
            }
            previousAttachPoint = attachPoint;
        }
        distance = branchLength - previousAttachPoint;
        if (distance > maxDistance) {
            maxDistance = distance;
            saveAttachPoint1 = previousAttachPoint;
            saveAttachPoint2 = branchLength;
        }
        float middle = (saveAttachPoint1 + saveAttachPoint2) / 2;
        float random = Randomizer.random1() * maxDistance;
        float newY = middle + random;
        return new Vector3f(0, newY, 0);
    }
