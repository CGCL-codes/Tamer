        private long findPosition(long from, long to, long ordinal) {
            long mid = (from + to) / 2;
            long offset = 100L;
            long start = Math.max(mid - offset, oversizedStart);
            reader.setPosition(start);
            if (start != oversizedStart) {
                boolean last = false;
                for (; start < mid; start++) {
                    boolean bit = reader.readBoolean();
                    if (bit && last) break;
                    last = bit;
                }
            }
            if (start == mid) throw new IllegalStateException("No consecutive set bits between " + (mid - offset) + " and mid");
            while (true) {
                long left = reader.getPosition();
                long a = coded.readPositiveLong();
                if ((a & 1L) == 1L) continue;
                long b = coded.readPositiveLong();
                long right = reader.getPosition();
                long ord = a / 2;
                long pos = (b - 1L) / 2;
                if (ord == ordinal) {
                    return pos;
                } else if (ordinal < ord) {
                    return left == oversizedStart ? -1L : findPosition(from, left, ordinal);
                } else if (right >= mid) {
                    return right == oversizedFinish ? -1L : findPosition(right, to, ordinal);
                }
            }
        }
