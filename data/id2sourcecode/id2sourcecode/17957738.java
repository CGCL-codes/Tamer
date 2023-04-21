        public void writeFrom(Object bytesMessage, Method read, int len) throws Exception {
            if (len <= 0) return;
            int x = size / unitSize;
            int y = size % unitSize;
            int n = len;
            byte[] dst = take();
            try {
                if (y > 0) {
                    n = unitSize - y;
                    read.invoke(bytesMessage, dst, n);
                    System.arraycopy(dst, 0, data.get(x), y, n);
                    if (n >= len) {
                        put(dst);
                        size += len;
                        return;
                    }
                    n = len - n;
                }
                data.add(dst);
                while ((n -= (Integer) read.invoke(bytesMessage, dst, unitSize)) > 0) {
                    dst = take();
                    data.add(dst);
                }
                size += len;
            } catch (Exception e) {
                trimToSize(size);
                throw e;
            }
        }
