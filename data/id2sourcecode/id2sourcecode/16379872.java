        public void run(FileState fstate, Integer block_num, Etree etree, Map<BlockAddress, Object> data_map, UserData user_data, Integer data_offset) {
            assert (user_data != null);
            assert (data_offset == 0);
            byte[] data = user_data.block.data;
            SecureHash read_hash = new SHA1Hash(data);
            SecureHash write_hash = fstate.blocks.get(block_num);
            if (!write_hash.equals(read_hash)) logger.fatal("Bytes read do not match bytes written: " + "write_hash=" + write_hash + " read_hash=" + read_hash); else logger.info("Done reading bytes: target=" + fstate.name + " block_num=" + block_num);
            block_num++;
            if ((block_num * BLOCK_SIZE) < fstate.size) read_bytes.run(fstate, block_num, etree, data_map); else {
                logger.info("Done processing read request: " + "target=" + fstate.name);
                sendRequest();
            }
            return;
        }
