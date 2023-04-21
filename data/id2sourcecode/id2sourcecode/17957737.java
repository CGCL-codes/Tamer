        public void readTo(Object bytesMessage, Method write) throws Exception {
            if (position >= size) return;
            int x = position / unitSize;
            int y = position % unitSize;
            for (; x < data.size(); x++) {
                int n = remaining();
                if (n > unitSize) n = unitSize;
                byte[] src = data.get(x);
                write.invoke(bytesMessage, src, y, n);
                y = 0;
            }
            position = size;
        }
