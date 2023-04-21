    public DriftRecord(TypedEvent e) {
        start = e.getLong(DRIFT_START_INDEX);
        middle = e.getLong(DRIFT_MIDDLE_INDEX);
        end = e.getLong(DRIFT_END_INDEX);
        total = end - start;
        error = total / 2;
        localMiddle = (end + start) / 2;
        drift = middle - localMiddle;
    }
