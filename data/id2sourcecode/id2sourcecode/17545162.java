    private Map<BlockAddress, DataBlock> readData(PublicKey pkey, BlockAddress[] addrs) {
        String key_prefix = root_dir + "/" + printPkey(pkey);
        Map<Long, SortedSet<BlockAddress>> map = new LinkedHashMap<Long, SortedSet<BlockAddress>>();
        Map<BlockAddress, DataBlock> map2 = new LinkedHashMap<BlockAddress, DataBlock>();
        for (BlockAddress addr : addrs) {
            SortedSet<BlockAddress> set = map.get((long) addr.getSeqNum());
            if (set == null) {
                set = new TreeSet<BlockAddress>();
                map.put((long) addr.getSeqNum(), set);
            }
            set.add(addr);
            map2.put(addr, (DataBlock) null);
        }
        for (Map.Entry<Long, SortedSet<BlockAddress>> entry : map.entrySet()) {
            Long seq_num = entry.getKey();
            SortedSet<BlockAddress> set = entry.getValue();
            BlockAddress[] addrs2 = new BlockAddress[set.size()];
            set.toArray(addrs2);
            String key = key_prefix + "_" + seq_num + ".ext";
            FileChannel fchannel = null;
            if (open_writeable_files.containsKey(key)) {
                fchannel = open_writeable_files.get(key);
            } else if (open_readonly_files.containsKey(key)) {
                fchannel = open_readonly_files.get(key);
            } else {
                File f = new File(key);
                assert f.exists() : key + " does not exist";
                RandomAccessFile file = null;
                try {
                    file = new RandomAccessFile(f, "r");
                } catch (IOException e) {
                    BUG("readData: could not creat file " + e);
                }
                fchannel = file.getChannel();
                Pair victim = open_readonly_files.put(key, fchannel);
                if (victim != null) {
                    String victim_key = (String) victim.first();
                    FileChannel victim_channel = (FileChannel) victim.second();
                    assert victim_channel.isOpen();
                    try {
                        victim_channel.close();
                    } catch (IOException e) {
                        BUG("readData: could not close victim file=" + victim_key + ", channel=" + victim_channel);
                    }
                }
            }
            Map<BlockAddress, DataBlockImpl> blocks = readFile(fchannel, addrs2, DataBlockImpl.class);
            map2.putAll(blocks);
            if (_compute_block_hash && _verify_block_hash) {
                for (Map.Entry<BlockAddress, DataBlockImpl> entry2 : blocks.entrySet()) {
                    BlockAddress addr = entry2.getKey();
                    DataBlock block = entry2.getValue();
                    BigInteger hash = LogUtils.computeBlockName(block);
                    assert hash.equals(addr.getBlockName()) : "hash(block)=0x" + guidToString(hash) + " != addr.hash=0x" + guidToString(addr.getBlockName()) + ". addr=" + addr + " block=" + block;
                }
            }
        }
        return map2;
    }
