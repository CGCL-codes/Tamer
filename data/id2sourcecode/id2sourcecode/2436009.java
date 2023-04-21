    int trailingZeros(int unit) {
        int ans = 0;
        while (unit % 10 == 0) {
            ans++;
            unit = unit / 10;
        }
        return ans;
    }
