    public boolean updateMeasurementGroups(List<MeasurementGroup> newMeasurementGroups) {
        boolean change = false;
        ArrayList<MeasurementGroup> deletedMeasurementGroups = new ArrayList<MeasurementGroup>();
        synchronized (measurementGroups) {
            for (MeasurementGroup oldMeasurementGroup : measurementGroups) if (!newMeasurementGroups.contains(oldMeasurementGroup)) {
                deletedMeasurementGroups.add(oldMeasurementGroup);
            }
            measurementGroups.removeAll(deletedMeasurementGroups);
        }
        for (MeasurementGroup removedMeasurementGroup : deletedMeasurementGroups) {
            synchronized (experimentChannels) {
                for (ExperimentChannel channel : experimentChannels) {
                    channel.removeMeasurementGroup(removedMeasurementGroup);
                }
            }
        }
        change = deletedMeasurementGroups.size() != 0;
        ArrayList<MeasurementGroup> addedMeasurementGroups = new ArrayList<MeasurementGroup>();
        for (MeasurementGroup newMeasurementGroup : newMeasurementGroups) if (!measurementGroups.contains(newMeasurementGroup)) addedMeasurementGroups.add(newMeasurementGroup);
        synchronized (measurementGroups) {
            measurementGroups.addAll(addedMeasurementGroups);
            for (MeasurementGroup measurementGroup : measurementGroups) for (ExperimentChannel channel : measurementGroup.getChannels()) channel.addMeasurementGroup(measurementGroup);
        }
        change |= addedMeasurementGroups.size() != 0;
        return change;
    }
