    public Vector<VRWEvent> process() {
        RangeDataSet ds = (RangeDataSet) getDataSet();
        int data[] = ds.getData();
        int resolution = ds.getResolution();
        boolean observation = false;
        beginIndexObstacle = -1;
        endIndexObstacle = -1;
        for (int i = 1; i < getDataSet().getLength(); i++) {
            if ((data[i] < (data[i - 1] - 100)) && (beginIndexObstacle == -1)) {
                beginRangeObstacle = data[i];
                beginIndexObstacle = i;
            }
            if ((Math.abs(data[i] - beginRangeObstacle) > 20) && (beginIndexObstacle != -1)) {
                endRangeObstacle = data[i - 1];
                endIndexObstacle = i - 1;
            } else continue;
            long distance = (beginRangeObstacle + endRangeObstacle) / 2;
            double width = endIndexObstacle - beginIndexObstacle;
            width = Math.sin(width * resolution * 3.1416 / 1800) * distance;
            if ((width < 5) || (width > 100)) {
                beginRangeObstacle = -1;
                endRangeObstacle = -1;
                beginIndexObstacle = -1;
                endIndexObstacle = -1;
                continue;
            }
            int middleIndex = (beginIndexObstacle + endIndexObstacle) / 2;
            int bearing = middleIndex * ds.getResolution();
            if (!ds.isClockwiseData()) {
                bearing = ds.getStartAngle() + bearing;
            } else bearing = ds.getStartAngle() + ds.getLength() * ds.getResolution() - bearing;
            observation = true;
        }
        if (observation) {
            senseObject = true;
        } else {
            if (senseObject) getObjectTracker().lost("unknown");
            senseObject = false;
        }
        return super.process();
    }
