    protected void monitorBeamEvents() {
        if (_beamEventChannel == null && _sequence != null) {
            final TimingCenter timingCenter = _sequence.getAccelerator().getTimingCenter();
            if (timingCenter != null) {
                _beamEventChannel = timingCenter.getChannel(TimingCenter.BEAM_ON_EVENT_HANDLE);
                _bpmCorrelator.addBeamEvent(_beamEventChannel);
            }
        }
    }
