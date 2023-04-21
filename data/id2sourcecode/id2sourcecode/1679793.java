    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case BmmPackage.COURSE_OF_ACTION__COMPOSED_OF:
                return ((InternalEList<?>) getComposedOf()).basicRemove(otherEnd, msgs);
            case BmmPackage.COURSE_OF_ACTION__PART_OF:
                return ((InternalEList<?>) getPartOf()).basicRemove(otherEnd, msgs);
            case BmmPackage.COURSE_OF_ACTION__ENABLED_BY:
                return ((InternalEList<?>) getEnabledBy()).basicRemove(otherEnd, msgs);
            case BmmPackage.COURSE_OF_ACTION__ENABLES:
                return ((InternalEList<?>) getEnables()).basicRemove(otherEnd, msgs);
            case BmmPackage.COURSE_OF_ACTION__CHANNELS_EFFORT_TOWARD:
                return ((InternalEList<?>) getChannelsEffortToward()).basicRemove(otherEnd, msgs);
            case BmmPackage.COURSE_OF_ACTION__REALIZED_BY:
                return ((InternalEList<?>) getRealizedBy()).basicRemove(otherEnd, msgs);
            case BmmPackage.COURSE_OF_ACTION__GOVERNED_BY:
                return ((InternalEList<?>) getGovernedBy()).basicRemove(otherEnd, msgs);
            case BmmPackage.COURSE_OF_ACTION__FORMULATED_BASED_ON:
                return ((InternalEList<?>) getFormulatedBasedOn()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }
