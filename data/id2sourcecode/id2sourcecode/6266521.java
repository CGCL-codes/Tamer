    public boolean ensure(KEY key, DATA data) {
        RunTime.assumedNotNull(key, data);
        DATA d1 = keyData.put(key, data);
        if (null != d1) {
            if (d1 != data) {
                RunTime.badCall("You attempted to overwrite an already existing key with a new value. Not acceptable");
            }
        }
        KEY k1 = dataKey.put(data, key);
        if (null == d1) {
            RunTime.assumedNull(k1);
        } else {
            RunTime.assumedTrue(k1 == key);
        }
        return (null != d1);
    }
