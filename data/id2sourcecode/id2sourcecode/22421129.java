    public double getHardLowerFieldLimit() {
        double lowLimit = Double.NaN;
        try {
            lowLimit = _powerSupply.getChannel(MagnetMainSupply.FIELD_SET_HANDLE).rawLowerDisplayLimit().doubleValue();
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (GetException e) {
            e.printStackTrace();
        } catch (NoSuchChannelException e) {
            e.printStackTrace();
        }
        return lowLimit;
    }
