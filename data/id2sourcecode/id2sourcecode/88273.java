    private void call() {
        int[] change = null;
        if (calls[callno] == 1) {
            change = currentmethod.getBob();
        }
        if (calls[callno] == 2) {
            change = currentmethod.getSingle();
        }
        int j = 0;
        for (int i = 0; i < thischange.length; ) {
            if (j < change.length && i == change[j] - 1) {
                i++;
                j++;
            } else {
                int temp = thischange[i];
                thischange[i] = thischange[i + 1];
                thischange[i + 1] = temp;
                i += 2;
                j += 1;
            }
        }
        callno++;
        nextmethod--;
        nextcall = currentmethod.callFreq();
    }
