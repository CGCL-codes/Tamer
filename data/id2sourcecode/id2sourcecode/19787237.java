    private void init(Object[] data) {
        if (data == null) {
            return;
        }
        if (!(data[0] instanceof String[])) {
            throw new IllegalArgumentException("Data sets passed to DataSetForApps " + "must begin with String[], with the " + "length of the array equal to the " + "number of attribute arrays that follow");
        }
        attributeNames = (String[]) data[0];
        aliases = new Map[attributeNames.length];
        dataSetFull = new Object[dataObjectOriginal.length + 1];
        dataSetFull[0] = attributeNames;
        int len = attributeNames.length;
        dataType = new int[len];
        numNumericAttributes = 0;
        for (int i = 0; i < len; i++) {
            if (data[i] instanceof SpatialWeights) {
                spatialWeights = (SpatialWeights) data[i];
            }
            if (data[i + 1] instanceof String[]) {
                String attrName = attributeNames[i].toLowerCase();
                if (attrName.endsWith("name")) {
                    dataType[i] = DataSetForApps.TYPE_NAME;
                    observationNames = (String[]) data[i + 1];
                }
            } else if (data[i + 1] instanceof double[]) {
                dataType[i] = DataSetForApps.TYPE_DOUBLE;
                numNumericAttributes++;
            } else if (data[i + 1] instanceof int[]) {
                dataType[i] = DataSetForApps.TYPE_INTEGER;
                numNumericAttributes++;
            } else if (data[i + 1] instanceof boolean[]) {
                dataType[i] = DataSetForApps.TYPE_BOOLEAN;
                numNumericAttributes++;
            } else {
                dataType[i] = DataSetForApps.TYPE_NONE;
            }
            dataSetFull[i + 1] = data[i + 1];
        }
        for (Object element : data) {
            if (element instanceof Shape[]) {
                Shape[] temp = ((Shape[]) element);
                if (temp[0] instanceof GeneralPathLine) {
                    spatialType = DataSetForApps.SPATIAL_TYPE_LINE;
                } else {
                    spatialType = DataSetForApps.SPATIAL_TYPE_POLYGON;
                    break;
                }
            } else if (element instanceof Point2D[]) {
                spatialType = DataSetForApps.SPATIAL_TYPE_POINT;
                break;
            } else if (element instanceof Geometry[]) {
                spatialType = DataSetForApps.SPATIAL_TYPE_GEOMETRY;
                break;
            } else if (element instanceof List<?>) {
                spatialType = DataSetForApps.SPATIAL_TYPE_MULTIPOINT;
                break;
            }
        }
        for (Object element : data) {
            if (element instanceof SpatialWeights) {
                spatialWeights = (SpatialWeights) element;
            }
        }
        int otherInfo = data.length - 1 - len;
        dataSetNumericAndSpatial = new Object[numNumericAttributes + 2 + otherInfo];
        dataSetNumeric = new Object[numNumericAttributes];
        if (otherInfo > 0) {
            for (int i = 0; i < otherInfo; i++) {
                dataSetNumericAndSpatial[numNumericAttributes + 2 + i] = data[len + 1 + i];
                dataSetFull[len + 2 + i] = data[len + 1 + i];
            }
        }
        attributeNamesNumeric = new String[numNumericAttributes];
        int dataTypeIndex = 0;
        for (int i = 0; i < numNumericAttributes; i++) {
            while ((dataType[dataTypeIndex]) < 1) {
                dataTypeIndex++;
            }
            dataSetNumericAndSpatial[i + 1] = data[dataTypeIndex + 1];
            dataSetNumeric[i] = data[dataTypeIndex + 1];
            attributeNamesNumeric[i] = attributeNames[dataTypeIndex];
            dataTypeIndex++;
        }
        dataSetNumericAndSpatial[0] = attributeNamesNumeric;
        if (observationNames != null) {
            dataSetNumericAndSpatial[numNumericAttributes + 1] = observationNames;
            dataSetFull[len + 1] = observationNames;
        } else {
            dataSetNumericAndSpatial[numNumericAttributes + 1] = null;
            dataSetFull[len + 1] = observationNames;
        }
        if (dataType[0] == DataSetForApps.TYPE_NAME) {
            numObservations = ((String[]) dataObjectOriginal[1]).length;
        } else if (dataType[0] == DataSetForApps.TYPE_DOUBLE) {
            numObservations = ((double[]) dataSetNumericAndSpatial[1]).length;
        } else if (dataType[0] == DataSetForApps.TYPE_INTEGER) {
            numObservations = ((int[]) dataSetNumericAndSpatial[1]).length;
        } else if (dataType[0] == DataSetForApps.TYPE_BOOLEAN) {
            numObservations = ((boolean[]) dataSetNumericAndSpatial[1]).length;
        }
    }
