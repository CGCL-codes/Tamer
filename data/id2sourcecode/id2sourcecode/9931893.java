    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case MeasuresPackage.LEVEL_HISTORY__DESCRIPTOR:
                return getDescriptor();
            case MeasuresPackage.LEVEL_HISTORY__CHANNEL:
                return getChannel();
        }
        return super.eGet(featureID, resolve, coreType);
    }
