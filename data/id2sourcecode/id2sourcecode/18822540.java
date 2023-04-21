    public CostsCentre getElement(int _idCostCentre) {
        int initIndex = 0;
        int endIndex = size() - 1;
        int midIndex = 0;
        while (initIndex <= endIndex) {
            midIndex = (initIndex + endIndex) / 2;
            int idCC = ((CostsCentre) elementAt(midIndex)).getIdCostCentre();
            if (_idCostCentre == idCC) {
                break;
            } else if (_idCostCentre > idCC) {
                initIndex = midIndex + 1;
            } else if (_idCostCentre < idCC) {
                endIndex = midIndex - 1;
            }
        }
        if (initIndex > endIndex) {
            return null;
        } else {
            return (CostsCentre) elementAt(midIndex);
        }
    }
