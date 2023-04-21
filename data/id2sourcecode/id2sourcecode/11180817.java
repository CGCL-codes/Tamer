    public void finalizeBoundaries() {
        if (highEnd - lowEnd == 0) {
            return;
        }
        double diff = highEnd - lowEnd;
        double diffPower = Math.floor(Math.log10(diff));
        double mean = (highEnd + lowEnd) / 2;
        double meanPower = Math.floor(Math.log10(mean));
        mean = mean / (Math.pow(10, meanPower - significantDigits + 1));
        diff = diff / (Math.pow(10, diffPower - significantDigits + 1));
        mean = Math.round(mean);
        diff = Math.ceil(diff);
        mean = mean * (Math.pow(10, meanPower - significantDigits + 1));
        diff = diff * (Math.pow(10, diffPower - significantDigits + 1));
        lowEnd = mean - (diff / 2);
        highEnd = mean + (diff / 2);
    }
