    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case BmmPackage.COURSE_OF_ACTION__COMPOSED_OF:
                getComposedOf().clear();
                return;
            case BmmPackage.COURSE_OF_ACTION__PART_OF:
                getPartOf().clear();
                return;
            case BmmPackage.COURSE_OF_ACTION__ENABLED_BY:
                getEnabledBy().clear();
                return;
            case BmmPackage.COURSE_OF_ACTION__ENABLES:
                getEnables().clear();
                return;
            case BmmPackage.COURSE_OF_ACTION__CHANNELS_EFFORT_TOWARD:
                getChannelsEffortToward().clear();
                return;
            case BmmPackage.COURSE_OF_ACTION__REALIZED_BY:
                getRealizedBy().clear();
                return;
            case BmmPackage.COURSE_OF_ACTION__GOVERNED_BY:
                getGovernedBy().clear();
                return;
            case BmmPackage.COURSE_OF_ACTION__FORMULATED_BASED_ON:
                getFormulatedBasedOn().clear();
                return;
        }
        super.eUnset(featureID);
    }
