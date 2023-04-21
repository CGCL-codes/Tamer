    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case BmmPackage.COURSE_OF_ACTION__COMPOSED_OF:
                return getComposedOf();
            case BmmPackage.COURSE_OF_ACTION__PART_OF:
                return getPartOf();
            case BmmPackage.COURSE_OF_ACTION__ENABLED_BY:
                return getEnabledBy();
            case BmmPackage.COURSE_OF_ACTION__ENABLES:
                return getEnables();
            case BmmPackage.COURSE_OF_ACTION__CHANNELS_EFFORT_TOWARD:
                return getChannelsEffortToward();
            case BmmPackage.COURSE_OF_ACTION__REALIZED_BY:
                return getRealizedBy();
            case BmmPackage.COURSE_OF_ACTION__GOVERNED_BY:
                return getGovernedBy();
            case BmmPackage.COURSE_OF_ACTION__FORMULATED_BASED_ON:
                return getFormulatedBasedOn();
        }
        return super.eGet(featureID, resolve, coreType);
    }
