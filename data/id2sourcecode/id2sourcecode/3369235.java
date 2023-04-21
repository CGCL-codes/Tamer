    protected Transferable createTransferable(JComponent c) {
        if (c instanceof MultiModeTable) {
            try {
                mms = ((MultiModeTable) c).getSelection();
                MultiModeSelection.MultiModeChannelSelection[] mmcs = mms.getChannelData();
                int[] selCols = mms.getSelCols();
                multiModeFlavor.clearGrid();
                multiModeFlavor.setDefCols(selCols);
                for (int i = 0, j = mmcs.length; i < j; i++) multiModeFlavor.addRow(mmcs[i].getChannel().intValue());
                return this;
            } catch (DeviceException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
