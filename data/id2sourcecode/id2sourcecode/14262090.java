    public void updateStats(Set types, Map values) {
        if (types.contains(AzureusCoreStats.ST_NET_WRITE_CONTROL_WAIT_COUNT)) {
            values.put(AzureusCoreStats.ST_NET_WRITE_CONTROL_WAIT_COUNT, new Long(wait_count));
        }
        if (types.contains(AzureusCoreStats.ST_NET_WRITE_CONTROL_NP_COUNT)) {
            values.put(AzureusCoreStats.ST_NET_WRITE_CONTROL_NP_COUNT, new Long(non_progress_count));
        }
        if (types.contains(AzureusCoreStats.ST_NET_WRITE_CONTROL_P_COUNT)) {
            values.put(AzureusCoreStats.ST_NET_WRITE_CONTROL_P_COUNT, new Long(progress_count));
        }
        if (types.contains(AzureusCoreStats.ST_NET_WRITE_CONTROL_ENTITY_COUNT)) {
            values.put(AzureusCoreStats.ST_NET_WRITE_CONTROL_ENTITY_COUNT, new Long(high_priority_entities.size() + boosted_priority_entities.size() + normal_priority_entities.size()));
        }
        if (types.contains(AzureusCoreStats.ST_NET_WRITE_CONTROL_CON_COUNT) || types.contains(AzureusCoreStats.ST_NET_WRITE_CONTROL_READY_CON_COUNT) || types.contains(AzureusCoreStats.ST_NET_WRITE_CONTROL_READY_BYTE_COUNT)) {
            long ready_bytes = 0;
            int ready_connections = 0;
            int connections = 0;
            ArrayList[] refs = { normal_priority_entities, boosted_priority_entities, high_priority_entities };
            for (int i = 0; i < refs.length; i++) {
                ArrayList ref = refs[i];
                for (int j = 0; j < ref.size(); j++) {
                    RateControlledEntity entity = (RateControlledEntity) ref.get(j);
                    connections += entity.getConnectionCount();
                    ready_connections += entity.getReadyConnectionCount(write_waiter);
                    ready_bytes += entity.getBytesReadyToWrite();
                }
            }
            values.put(AzureusCoreStats.ST_NET_WRITE_CONTROL_CON_COUNT, new Long(connections));
            values.put(AzureusCoreStats.ST_NET_WRITE_CONTROL_READY_CON_COUNT, new Long(ready_connections));
            values.put(AzureusCoreStats.ST_NET_WRITE_CONTROL_READY_BYTE_COUNT, new Long(ready_bytes));
        }
    }
