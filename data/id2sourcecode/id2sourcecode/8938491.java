    public void updateChart() {
        List<String> signals = new ArrayList<String>(_controller.getSelectedSignals());
        final int numSignals = signals.size();
        final MachineSnapshot[] machineSnapshots = _model.getSnapshots();
        final Map<String, List<ChannelSnapshot>> signalMap = new HashMap<String, List<ChannelSnapshot>>(numSignals);
        for (int signalIndex = 0; signalIndex < numSignals; signalIndex++) {
            final String signal = signals.get(signalIndex);
            signalMap.put(signal, new ArrayList<ChannelSnapshot>());
        }
        _model.populateSnapshots();
        for (int machineSnapshotIndex = 0; machineSnapshotIndex < machineSnapshots.length; machineSnapshotIndex++) {
            final MachineSnapshot machineSnapshot = machineSnapshots[machineSnapshotIndex];
            final ChannelSnapshot[] channelSnapshots = machineSnapshot.getChannelSnapshots();
            for (int index = 0; index < channelSnapshots.length; index++) {
                final ChannelSnapshot channelSnapshot = channelSnapshots[index];
                final String signal = channelSnapshot.getPV();
                final List<ChannelSnapshot> dataList = signalMap.get(signal);
                if (dataList != null) {
                    dataList.add(channelSnapshot);
                }
            }
        }
        Vector<BasicGraphData> seriesData = new Vector<BasicGraphData>();
        for (int signalIndex = 0; signalIndex < numSignals; signalIndex++) {
            final String signal = signals.get(signalIndex);
            List<ChannelSnapshot> snapshots = signalMap.get(signal);
            final int numPoints = snapshots.size();
            double[] values = new double[numPoints];
            double[] timestamps = new double[numPoints];
            for (int pointIndex = 0; pointIndex < numPoints; pointIndex++) {
                ChannelSnapshot snapshot = snapshots.get(pointIndex);
                values[pointIndex] = snapshot.getValue()[0];
                timestamps[pointIndex] = snapshot.getTimestamp().getSeconds();
            }
            if (numPoints > 0) {
                Color color = IncrementalColors.getColor(signalIndex);
                BasicGraphData graphData = new BasicGraphData();
                graphData.addPoint(timestamps, values);
                graphData.setGraphColor(color);
                graphData.setGraphProperty(_chart.getLegendKeyString(), signal);
                graphData.setGraphName(signal);
                seriesData.add(graphData);
            }
        }
        _chart.removeAllGraphData();
        _chart.addGraphData(seriesData);
        double stepSize = (machineSnapshots[machineSnapshots.length - 1].getTimestamp().getTime() / 1000. - machineSnapshots[0].getTimestamp().getTime() / 1000.) / 3.;
        _chart.setLimitsAndTicksX(machineSnapshots[0].getTimestamp().getTime() / 1000., machineSnapshots[machineSnapshots.length - 1].getTimestamp().getTime() / 1000., stepSize);
    }
