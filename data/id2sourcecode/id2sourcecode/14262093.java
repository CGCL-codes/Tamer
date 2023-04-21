    private boolean doHighPriorityWrite() {
        RateControlledEntity ready_entity = getNextReadyHighPriorityEntity();
        if (ready_entity != null) {
            if (ready_entity.doProcessing(write_waiter, 0) > 0) {
                progress_count++;
                return true;
            } else {
                non_progress_count++;
                if (AGGRESIVE_WRITE) {
                    aggressive_np_high_priority_count++;
                    if (aggressive_np_high_priority_count < high_priority_entities.size()) {
                        return (true);
                    } else {
                        aggressive_np_high_priority_count = 0;
                    }
                }
            }
        }
        return false;
    }
