    public boolean update() {
        if (_lock.tryLock()) {
            try {
                Date lastLoggerEventTime = _remoteProxy.getLastLoggerEventTime(_groupType);
                if (!lastLoggerEventTime.equals(_lastLoggerEventTime)) {
                    boolean hasUpdate = false;
                    final boolean isLogging = _remoteProxy.isLogging(_groupType);
                    if (isLogging != _isLogging) {
                        _isLogging = isLogging;
                        hasUpdate = true;
                    }
                    final double loggingPeriod = _remoteProxy.getLoggingPeriod(_groupType);
                    if (loggingPeriod != _loggingPeriod) {
                        _loggingPeriod = loggingPeriod;
                        hasUpdate = true;
                    }
                    if (hasUpdate) {
                        _postProxy.loggerSessionUpdated(this);
                    }
                    _latestSnapshotTimestamp = _remoteProxy.getTimestampOfLastPublishedSnapshot(_groupType);
                    _latestSnapshotDump = _remoteProxy.getLastPublishedSnapshotDump(_groupType);
                    _lastLoggerEventTime = lastLoggerEventTime;
                    _postProxy.snapshotPublished(this, _latestSnapshotTimestamp, _latestSnapshotDump);
                }
                Date lastChannelEventTime = _remoteProxy.getLastChannelEventTime(_groupType);
                if (!lastChannelEventTime.equals(_lastChannelEventTime)) {
                    List channelTables = _remoteProxy.getChannels(_groupType);
                    _lastChannelEventTime = lastChannelEventTime;
                    processChannels(channelTables);
                }
                _postProxy.loggerSessionUpdated(this);
                return true;
            } catch (Exception exception) {
                System.err.println("Got an update exception...");
                System.err.println(exception);
                return true;
            } finally {
                _lock.unlock();
            }
        }
        return false;
    }
