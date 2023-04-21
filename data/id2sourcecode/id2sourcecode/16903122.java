    public boolean uploadValue(final LiveParameter parameter, final double value) {
        final Electromagnet bend = (Electromagnet) parameter.getNode();
        final Channel controlChannel = getControlChannel(parameter.getNodeAgent());
        Channel bookChannel;
        try {
            bookChannel = parameter.getNode().getChannel(MagnetMainSupply.FIELD_BOOK_HANDLE);
        } catch (NoSuchChannelException exception) {
            Logger.getLogger("global").log(Level.WARNING, "Field book channel for node " + parameter.getNodeAgent().getID() + " cannot be found.");
            return false;
        }
        if (!controlChannel.isConnected()) {
            Logger.getLogger("global").log(Level.WARNING, controlChannel + " is not connected!");
            return false;
        } else if (!bookChannel.isConnected()) {
            Logger.getLogger("global").log(Level.WARNING, bookChannel + " is not connected!");
            return false;
        } else {
            try {
                final double caValue = toCA(parameter.getNodeAgent(), parameter.getInitialValue());
                controlChannel.putVal(caValue);
                bookChannel.putVal(caValue);
                return true;
            } catch (gov.sns.ca.PutException exception) {
                exception.printStackTrace();
                Logger.getLogger("global").log(Level.SEVERE, "Quadrupole field upload request failed for " + parameter.getNode(), exception);
                return false;
            } catch (gov.sns.ca.ConnectionException exception) {
                exception.printStackTrace();
                Logger.getLogger("global").log(Level.SEVERE, "Quadrupole field upload request failed for " + parameter.getNode(), exception);
                return false;
            }
        }
    }
