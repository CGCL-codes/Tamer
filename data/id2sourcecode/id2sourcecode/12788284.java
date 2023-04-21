    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case NetworkPackage.NETWORK__NODES:
                return ((InternalEList<?>) getNodes()).basicRemove(otherEnd, msgs);
            case NetworkPackage.NETWORK__CHANNELS:
                return ((InternalEList<?>) getChannels()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }
