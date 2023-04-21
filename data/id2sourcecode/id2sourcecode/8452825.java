    protected TCPNetworkManager() {
        Set types = new HashSet();
        types.add(AzureusCoreStats.ST_NET_TCP_SELECT_READ_COUNT);
        types.add(AzureusCoreStats.ST_NET_TCP_SELECT_WRITE_COUNT);
        AzureusCoreStats.registerProvider(types, new AzureusCoreStatsProvider() {

            public void updateStats(Set types, Map values) {
                if (types.contains(AzureusCoreStats.ST_NET_TCP_SELECT_READ_COUNT)) {
                    values.put(AzureusCoreStats.ST_NET_TCP_SELECT_READ_COUNT, new Long(read_select_count));
                }
                if (types.contains(AzureusCoreStats.ST_NET_TCP_SELECT_WRITE_COUNT)) {
                    values.put(AzureusCoreStats.ST_NET_TCP_SELECT_WRITE_COUNT, new Long(write_select_count));
                }
            }
        });
        AEThread2 read_selector_thread = new AEThread2("ReadController:ReadSelector", true) {

            public void run() {
                while (true) {
                    try {
                        if (READ_SELECT_MIN_LOOP_TIME > 0) {
                            long start = SystemTime.getHighPrecisionCounter();
                            read_selector.select(READ_SELECT_LOOP_TIME);
                            long duration = SystemTime.getHighPrecisionCounter() - start;
                            duration = duration / 1000000;
                            long sleep = READ_SELECT_MIN_LOOP_TIME - duration;
                            if (sleep > 0) {
                                try {
                                    Thread.sleep(sleep);
                                } catch (Throwable e) {
                                }
                            }
                        } else {
                            read_selector.select(READ_SELECT_LOOP_TIME);
                        }
                        read_select_count++;
                    } catch (Throwable t) {
                        Debug.out("readSelectorLoop() EXCEPTION: ", t);
                    }
                }
            }
        };
        read_selector_thread.setPriority(Thread.MAX_PRIORITY - 2);
        read_selector_thread.start();
        AEThread2 write_selector_thread = new AEThread2("WriteController:WriteSelector", true) {

            public void run() {
                while (true) {
                    try {
                        if (WRITE_SELECT_MIN_LOOP_TIME > 0) {
                            long start = SystemTime.getHighPrecisionCounter();
                            write_selector.select(WRITE_SELECT_LOOP_TIME);
                            long duration = SystemTime.getHighPrecisionCounter() - start;
                            duration = duration / 1000000;
                            long sleep = WRITE_SELECT_MIN_LOOP_TIME - duration;
                            if (sleep > 0) {
                                try {
                                    Thread.sleep(sleep);
                                } catch (Throwable e) {
                                }
                            }
                        } else {
                            write_selector.select(WRITE_SELECT_LOOP_TIME);
                            write_select_count++;
                        }
                    } catch (Throwable t) {
                        Debug.out("writeSelectorLoop() EXCEPTION: ", t);
                    }
                }
            }
        };
        write_selector_thread.setPriority(Thread.MAX_PRIORITY - 2);
        write_selector_thread.start();
    }
