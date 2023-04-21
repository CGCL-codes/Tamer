    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case BmmPackage.COURSE_OF_ACTION__COMPOSED_OF:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getComposedOf()).basicAdd(otherEnd, msgs);
            case BmmPackage.COURSE_OF_ACTION__PART_OF:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getPartOf()).basicAdd(otherEnd, msgs);
            case BmmPackage.COURSE_OF_ACTION__ENABLED_BY:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getEnabledBy()).basicAdd(otherEnd, msgs);
            case BmmPackage.COURSE_OF_ACTION__ENABLES:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getEnables()).basicAdd(otherEnd, msgs);
            case BmmPackage.COURSE_OF_ACTION__CHANNELS_EFFORT_TOWARD:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getChannelsEffortToward()).basicAdd(otherEnd, msgs);
            case BmmPackage.COURSE_OF_ACTION__REALIZED_BY:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getRealizedBy()).basicAdd(otherEnd, msgs);
            case BmmPackage.COURSE_OF_ACTION__GOVERNED_BY:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getGovernedBy()).basicAdd(otherEnd, msgs);
            case BmmPackage.COURSE_OF_ACTION__FORMULATED_BASED_ON:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getFormulatedBasedOn()).basicAdd(otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }
