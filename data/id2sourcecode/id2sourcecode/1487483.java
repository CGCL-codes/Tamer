    private void get_frags(LinkedList<ByteBuffer> result) {
        int record_marker = read_int();
        int size = record_marker & 0x7fffffff;
        boolean last_frag = (record_marker & 0x80000000) != 0;
        assert _bytes_available >= size : "bytes_avail=" + _bytes_available + " < size=" + size;
        ByteBuffer packet = in_order_packets.getFirst();
        int remaining = size;
        while (remaining > 0 && remaining > packet.remaining()) {
            if (logger.isDebugEnabled() && packet.hasArray()) _md.update(packet.array(), packet.arrayOffset(), packet.limit());
            _frags_packet_index++;
            in_order_packets.removeFirst();
            result.addLast(packet);
            remaining -= packet.remaining();
            ByteBuffer packet2 = in_order_packets.getFirst();
            if (logger.isDebugEnabled()) logger.debug("get_frags: (1) size=" + size + " remaining=" + remaining + " bytes_avail=" + _bytes_available + " splicing=" + _splicing + ". packet=" + packet + " packet2=" + packet2);
            packet = packet2;
            _spliced.addLast(_splicing ? Boolean.TRUE : Boolean.FALSE);
            if (_splicing) _splicing = false;
        }
        {
            ByteBuffer packet2 = in_order_packets.removeFirst();
            if (logger.isDebugEnabled()) logger.debug("get_frags: (2) size=" + size + " remaining=" + remaining + " bytes_avail=" + _bytes_available + " splicing=" + _splicing + ". packet=" + packet + " packet2=" + packet2);
        }
        if (remaining > 0) {
            if (remaining == packet.remaining()) {
                if (logger.isDebugEnabled() && packet.hasArray()) _md.update(packet.array(), packet.arrayOffset(), packet.limit());
                _frags_packet_index++;
                result.addLast(packet);
                remaining -= packet.remaining();
                if (logger.isDebugEnabled()) logger.debug("get_frags: (3) size=" + size + " remaining=" + remaining + " bytes_avail=" + _bytes_available + " splicing=" + _splicing + ". packet=" + packet);
                _spliced.addLast(_splicing ? Boolean.TRUE : Boolean.FALSE);
                if (_splicing) _splicing = false;
            } else {
                packet.mark();
                packet.position(packet.position() + remaining);
                ByteBuffer packet2 = packet.slice();
                packet.reset();
                packet.limit(packet.position() + remaining);
                if (logger.isDebugEnabled() && packet.hasArray()) _md.update(packet.array(), packet.arrayOffset(), packet.limit());
                _frags_packet_index++;
                result.addLast(packet);
                remaining -= packet.remaining();
                in_order_packets.addFirst(packet2);
                if (logger.isDebugEnabled()) logger.debug("get_frags: (4) size=" + size + " remaining=" + remaining + " bytes_avail=" + _bytes_available + " splicing=" + _splicing + ". packet=" + packet + " packet2=" + packet2 + " packet[len-8..len]=" + (packet.hasArray() ? ostore.util.ByteUtils.print_bytes(packet.array(), (packet.limit() > 8 ? packet.arrayOffset() + packet.limit() - 8 : packet.arrayOffset()), (packet.limit() > 8 ? 8 : packet.limit())) : "no backing array") + " packet2[0..8]=" + (packet2.hasArray() ? ostore.util.ByteUtils.print_bytes(packet2.array(), packet2.arrayOffset() + packet2.position(), (packet2.remaining() < 8 ? packet2.remaining() : 8)) : "no backing array"));
                _spliced.addLast(Boolean.TRUE);
                _splicing = true;
            }
        }
        assert remaining == 0 : "remaining=" + remaining + ". should be 0.";
        _bytes_available -= size;
        _frags_size += size;
        _frags_num++;
        _frags_end_index.addLast(_frags_packet_index);
        if (logger.isDebugEnabled()) {
            if (packet.hasArray()) {
                byte[] digest = _md.digest();
                logger.debug("get_frags: frag " + (_frags_num - 1) + " hash: 0x" + ostore.util.ByteUtils.print_bytes(digest, 0, 4));
            }
        }
    }
