    private static PeakList mergePeakLists(final File[] in, final File output, final boolean peakListRequired) throws Exception {
        PeakListReader reader = null;
        PeakListWriter writer = null;
        final PeakList peakList = new PeakList();
        PropertyChangeListener propertyChangeListener = null;
        try {
            writer = PeakListWriterFactory.newInstance(output);
            for (int i = 0; i < in.length; i++) {
                reader = PeakListReaderFactory.newInstance(in[i]);
                reader.addPropertyChangeListener(writer);
                if (peakListRequired) {
                    propertyChangeListener = new PropertyChangeListener() {

                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            if (evt.getPropertyName().equals(PeakListReader.ADD) && evt.getNewValue() instanceof Peak) {
                                peakList.add((Peak) evt.getNewValue());
                            }
                        }
                    };
                    reader.addPropertyChangeListener(propertyChangeListener);
                }
                reader.readPeaks();
                reader.close();
            }
            return peakListRequired ? peakList : null;
        } finally {
            if (reader != null) {
                reader.close();
                reader.removePropertyChangeListener(writer);
                reader.removePropertyChangeListener(propertyChangeListener);
            }
            if (writer != null) {
                writer.close();
            }
        }
    }
