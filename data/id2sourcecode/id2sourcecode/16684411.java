    private boolean configureItViaConfigureMenu(JCheckBoxMenuItem cmi) {
        if (!cmi.isSelected()) {
            cmi.doClick();
            Utils.logToConsole("TWS has been configured to accept API connections.");
            mChannel.writeAck("configured");
        } else {
            Utils.logToConsole("TWS is already configured to accept API connections.");
            mChannel.writeAck("already configured");
        }
        return true;
    }
