    public double getHardUpperFieldLimit() {
        double uppLimit = Double.NaN;
        try {
            uppLimit = _powerSupply.getChannel(MagnetMainSupply.FIELD_SET_HANDLE).rawUpperDisplayLimit().doubleValue();
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (GetException e) {
            e.printStackTrace();
        } catch (NoSuchChannelException e) {
            e.printStackTrace();
        }
        return uppLimit;
    }
