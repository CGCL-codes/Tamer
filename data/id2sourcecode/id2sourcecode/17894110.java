    public String prepareConvert(File source, File destination) {
        if (!PathJPanel.isGif(source)) {
            return "Source File not a .gif";
        }
        try {
            if (destination.exists()) {
                int i = JOptionPane.showConfirmDialog(EJFrame.getEJFrame(), "\"" + destination.getName() + "\"" + " already exists, overwrite this file?", "File already exists", JOptionPane.YES_NO_CANCEL_OPTION);
                if (i == JOptionPane.OK_OPTION) {
                    destination.delete();
                    destination.createNewFile();
                } else if (i == JOptionPane.NO_OPTION) {
                    return null;
                } else if (i == JOptionPane.CANCEL_OPTION || i < 0) {
                    return EJFrame.CANCEL_STRING;
                }
            }
            Start.convert(source, destination, EJFrame.getEJFrame());
        } catch (Exception e) {
            if (e instanceof CancellationException) {
                return EJFrame.CANCEL_STRING;
            }
            String errmsg = e.getMessage();
            String errtype = "";
            if (errmsg.contains("reading")) {
                errtype = " while reading";
            } else if (errmsg.contains("writing")) {
                errtype = " while writing";
            }
            if (e.getCause() != null && errtype.length() != 0) {
                errmsg = e.getCause().getMessage();
            }
            System.err.println(errmsg);
            return "Error" + errtype + ": " + errmsg;
        }
        return null;
    }
