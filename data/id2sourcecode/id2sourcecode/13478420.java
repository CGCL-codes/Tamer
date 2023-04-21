    private void updateActualArea(MouseEvent e) {
        try {
            AChannel ch = getSelectedLayer().getChannel(e.getPoint());
            if (ch != null) {
                actualChannel = getSelectedLayer().getChannel(e.getPoint());
                area = ((Cookie) actualChannel.getCookies().getCookie(getName())).area;
            }
        } catch (Exception exc) {
            Debug.printStackTrace(5, exc);
        }
    }
