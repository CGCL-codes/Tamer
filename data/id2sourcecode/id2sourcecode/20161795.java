    public void setBigEndian(boolean bigEndian) {
        if (bigEndian != this.bigEndian) {
            byte tmp;
            for (int i = 0; i < data.length; i += 2) {
                tmp = data[i];
                data[i] = data[i + 1];
                data[i + 1] = tmp;
            }
            this.bigEndian = bigEndian;
        }
    }
