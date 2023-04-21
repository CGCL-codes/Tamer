    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case BmmPackage.COURSE_OF_ACTION__COMPOSED_OF:
                getComposedOf().clear();
                getComposedOf().addAll((Collection<? extends CourseOfAction>) newValue);
                return;
            case BmmPackage.COURSE_OF_ACTION__PART_OF:
                getPartOf().clear();
                getPartOf().addAll((Collection<? extends CourseOfAction>) newValue);
                return;
            case BmmPackage.COURSE_OF_ACTION__ENABLED_BY:
                getEnabledBy().clear();
                getEnabledBy().addAll((Collection<? extends CourseOfAction>) newValue);
                return;
            case BmmPackage.COURSE_OF_ACTION__ENABLES:
                getEnables().clear();
                getEnables().addAll((Collection<? extends CourseOfAction>) newValue);
                return;
            case BmmPackage.COURSE_OF_ACTION__CHANNELS_EFFORT_TOWARD:
                getChannelsEffortToward().clear();
                getChannelsEffortToward().addAll((Collection<? extends DesiredResult>) newValue);
                return;
            case BmmPackage.COURSE_OF_ACTION__REALIZED_BY:
                getRealizedBy().clear();
                getRealizedBy().addAll((Collection<? extends BusinessProcess>) newValue);
                return;
            case BmmPackage.COURSE_OF_ACTION__GOVERNED_BY:
                getGovernedBy().clear();
                getGovernedBy().addAll((Collection<? extends Directive>) newValue);
                return;
            case BmmPackage.COURSE_OF_ACTION__FORMULATED_BASED_ON:
                getFormulatedBasedOn().clear();
                getFormulatedBasedOn().addAll((Collection<? extends Directive>) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }
