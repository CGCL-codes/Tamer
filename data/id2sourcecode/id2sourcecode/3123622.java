        private void remove(int index) {
            Scan removed = scans[index];
            for (int i = index; i < length - 1; i++) {
                scans[i] = scans[i + 1];
            }
            scans[length - 1] = removed;
            length--;
        }
