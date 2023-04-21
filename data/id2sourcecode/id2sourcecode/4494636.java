        private void transferFrom(Ip4 ip) {
            ip.transferTo(this, 0, 20, 0);
            header.peer(this, 0, 20);
            header.hlen(5);
            header.clearFlags(Ip4.FLAG_MORE_FRAGMENTS);
            header.offset(0);
            header.checksum(0);
        }
