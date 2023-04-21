        public void run(Thunk1<BlockAddress[]> cb, Map<Integer, BigInteger> offset_name_map, DataBlock[] blocks, BigInteger[] bnames, BlockAddress[] db_baddrs) {
            Map<BigInteger, BlockAddress> name_addr_map = new HashMap<BigInteger, BlockAddress>();
            Set<DataBlock> to_write_set = new HashSet<DataBlock>();
            for (int i = 0; i < blocks.length; ++i) {
                if (db_baddrs[i] != null) {
                    name_addr_map.put(bnames[i], db_baddrs[i]);
                } else {
                    to_write_set.add(blocks[i]);
                }
            }
            if (logger.isDebugEnabled()) logger.debug("Done checking local db: " + "blocks_already_written=" + name_addr_map.size() + ", blocks_to_write=" + to_write_set.size());
            if (to_write_set.size() > 0) {
                DataBlock[] to_write = new DataBlock[to_write_set.size()];
                to_write = to_write_set.toArray(to_write);
                Thunk1<BlockAddress[]> write_cb = curry(write_blocks_cb, cb, offset_name_map, name_addr_map);
                log_int.write(pkey, skey, to_write, write_cb, flush_blocks_cb);
            } else {
                BlockAddress[] written = new BlockAddress[0];
                write_blocks_cb.run(cb, offset_name_map, name_addr_map, written);
            }
            return;
        }
