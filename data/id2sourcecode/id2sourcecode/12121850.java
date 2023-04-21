    public void markedDataArrived(Module src, Buffer buffer) {
        if (src instanceof BasicSourceModule) {
            markedDataStartTime = getMediaNanoseconds();
        } else {
            long t = getMediaNanoseconds() - markedDataStartTime;
            if (t > 0 && t < 1000000000) {
                if (!reportOnce) {
                    Log.comment("Computed latency for video: " + t / 1000000 + " ms\n");
                    reportOnce = true;
                }
                latency = (t + latency) / 2;
            }
        }
    }
