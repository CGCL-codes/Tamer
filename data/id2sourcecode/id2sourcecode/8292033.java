    public void propertyChange(java.beans.PropertyChangeEvent e) {
        if (_programmingPane == null) {
            log.warn("unexpected propertyChange: " + e);
            return;
        } else if (log.isDebugEnabled()) log.debug("property changed: " + e.getPropertyName() + " new value: " + e.getNewValue());
        log.debug("check valid: " + (e.getSource() == _programmingPane) + " " + (!e.getPropertyName().equals("Busy")) + " " + (((Boolean) e.getNewValue()).equals(Boolean.FALSE)));
        if (e.getSource() == _programmingPane && e.getPropertyName().equals("Busy") && ((Boolean) e.getNewValue()).equals(Boolean.FALSE)) {
            if (log.isDebugEnabled()) log.debug("end of a programming pane operation, remove");
            _programmingPane.removePropertyChangeListener(this);
            _programmingPane = null;
            if (_read && readChangesButton.isSelected()) {
                if (log.isDebugEnabled()) log.debug("restart readChanges");
                doRead();
            } else if (_read && readAllButton.isSelected()) {
                if (log.isDebugEnabled()) log.debug("restart readAll");
                doRead();
            } else if (writeChangesButton.isSelected()) {
                if (log.isDebugEnabled()) log.debug("restart writeChanges");
                doWrite();
            } else if (writeAllButton.isSelected()) {
                if (log.isDebugEnabled()) log.debug("restart writeAll");
                doWrite();
            } else {
                if (log.isDebugEnabled()) log.debug("read/write end because button is lifted");
                setBusy(false);
            }
        }
    }
