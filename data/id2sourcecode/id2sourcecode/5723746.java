    public void transferSelectedReadToWrite() {
        if (rows == null) {
            return;
        }
        for (int i = 0; i < rows.length; i++) {
            SnapshotAttribute attr = this.getSnapshotAttributeAtRow(i);
            SnapshotAttributeReadValue readValue = attr.getReadValue();
            SnapshotAttributeWriteValue writeValue = attr.getWriteValue();
            if (writeValue.isSettable() && (!writeValue.isNotApplicable()) && (!readValue.isNotApplicable())) {
                switch(attr.getData_format()) {
                    case AttrDataFormat._SCALAR:
                        Object scalarValue = readValue.getScalarValue();
                        if (scalarValue != null) {
                            setValueAt(scalarValue, i, 1);
                        }
                        break;
                    case AttrDataFormat._SPECTRUM:
                        Object[] spectrumValue = readValue.getSpectrumValue();
                        if (spectrumValue != null) {
                            setValueAt(spectrumValue, i, 1);
                        }
                        break;
                    case AttrDataFormat._IMAGE:
                        Object[] imageValue = readValue.getImageValue();
                        if (imageValue != null) {
                            setValueAt(imageValue, i, 1);
                        }
                        break;
                }
            }
        }
    }
