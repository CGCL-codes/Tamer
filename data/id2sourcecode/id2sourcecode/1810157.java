        public V getValue() {
            cnt++;
            long t = lastAccessTime;
            lastAccessTime = System.currentTimeMillis();
            t = lastAccessTime - t;
            average = (average + t) / 2;
            return super.getValue();
        }
