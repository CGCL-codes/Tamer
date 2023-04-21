    void updateHashtable(int to, int from) {
        byte[] data = new byte[3];
        int hash;
        Link temp;
        for (int i = to; i < from; i++) {
            if (i + MIN_LENGTH > inLength) {
                break;
            }
            data[0] = in[i];
            data[1] = in[i + 1];
            data[2] = in[i + 2];
            hash = hash(data);
            if (window[nextWindow].previous != null) {
                window[nextWindow].previous.next = null;
            } else if (window[nextWindow].hash != 0) {
                hashtable[window[nextWindow].hash].next = null;
            }
            window[nextWindow].hash = hash;
            window[nextWindow].value = i;
            window[nextWindow].previous = null;
            temp = window[nextWindow].next = hashtable[hash].next;
            hashtable[hash].next = window[nextWindow];
            if (temp != null) {
                temp.previous = window[nextWindow];
            }
            nextWindow = nextWindow + 1;
            if (nextWindow == WINDOW) {
                nextWindow = 0;
            }
        }
    }
