    protected synchronized VersionedSlot setValue(Version v, Object newValue) {
        int min = 0;
        int max = versionsLog.size();
        int index;
        if (lastGetVersion == v) lastGetValue = newValue;
        while (min < max) {
            index = (min + max) / 2;
            Version v2 = (Version) versionsLog.elementAt(index);
            if (v.precedes(v2)) {
                max = index;
            } else {
                if (v2.equals(v)) {
                    valuesLog.setElementAt(newValue, index);
                    return this;
                }
                min = index + 1;
            }
        }
        index = min;
        if (verboseDebug) {
            Object former_value = index > 0 ? valuesLog.elementAt(index - 1) : initialValue;
            if (newValue == former_value || (newValue != null && newValue.equals(former_value))) {
                System.out.print("Inserting redundant " + v + " : " + newValue + " into ");
                describe(System.out);
            }
        }
        Version nextInOrder = v.getNextInPreorderNoKids();
        Version nextInLog;
        if (index >= versionsLog.size()) {
            nextInLog = null;
        } else {
            nextInLog = (Version) versionsLog.elementAt(index);
        }
        if (nextInOrder != null && !nextInOrder.equals(nextInLog) && (nextInLog == null || !nextInLog.comesFrom(v))) {
            Object former_value = index > 0 ? valuesLog.elementAt(index - 1) : initialValue;
            valuesLog.insertElementAt(former_value, index);
            versionsLog.insertElementAt(nextInOrder, index);
        }
        valuesLog.insertElementAt(newValue, index);
        versionsLog.insertElementAt(v, index);
        return this;
    }
