    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case NetworkPackage.NETWORK__NODES:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getNodes()).basicAdd(otherEnd, msgs);
            case NetworkPackage.NETWORK__CHANNELS:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getChannels()).basicAdd(otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }
