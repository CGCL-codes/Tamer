    protected synchronized void addPing(int x, int y, int rtt, boolean re_base) {
        x = x / SPEED_DIVISOR;
        y = y / SPEED_DIVISOR;
        if (x > 65535) x = 65535;
        if (y > 65535) y = 65535;
        if (rtt > 65535) rtt = variance ? VARIANCE_MAX : RTT_MAX;
        if (rtt == 0) rtt = 1;
        int average_x = (x + last_x) / 2;
        int average_y = (y + last_y) / 2;
        last_x = x;
        last_y = y;
        x = average_x;
        y = average_y;
        int metric;
        if (variance) {
            if (re_base) {
                log("Re-based variance");
                recent_metrics_next = 0;
            }
            recent_metrics[recent_metrics_next++ % recent_metrics.length] = rtt;
            int var_metric = 0;
            int rtt_metric = 0;
            if (recent_metrics_next > 1) {
                int entries = Math.min(recent_metrics_next, recent_metrics.length);
                int total = 0;
                for (int i = 0; i < entries; i++) {
                    total += recent_metrics[i];
                }
                int average = total / entries;
                int total_deviation = 0;
                for (int i = 0; i < entries; i++) {
                    int deviation = recent_metrics[i] - average;
                    total_deviation += deviation * deviation;
                }
                var_metric = (int) Math.sqrt(total_deviation);
                if (entries == recent_metrics.length) {
                    int total_rtt = 0;
                    for (int i = 0; i < entries; i++) {
                        total_rtt += recent_metrics[i];
                    }
                    int average_rtt = total_rtt / recent_metrics.length;
                    if (average_rtt >= RTT_BAD_MAX) {
                        rtt_metric = VARIANCE_BAD_VALUE;
                    } else if (average_rtt > RTT_BAD_MIN) {
                        int rtt_diff = RTT_BAD_MAX - RTT_BAD_MIN;
                        int rtt_base = average_rtt - RTT_BAD_MIN;
                        rtt_metric = VARIANCE_GOOD_VALUE + ((VARIANCE_BAD_VALUE - VARIANCE_GOOD_VALUE) * rtt_base) / rtt_diff;
                    }
                }
            }
            metric = Math.max(var_metric, rtt_metric);
            if (metric < VARIANCE_BAD_VALUE) {
                addSpeedSupport(x, y);
            } else {
                addSpeedSupport(0, 0);
            }
        } else {
            metric = rtt;
        }
        region new_region = addPingSupport(x, y, rtt, metric);
        updateLimitEstimates();
        if (variance) {
            String up_e = getShortString(getEstimatedUploadLimit(false)) + "," + getShortString(getEstimatedUploadLimit(true)) + "," + getShortString(getEstimatedUploadCapacityBytesPerSec());
            String down_e = getShortString(getEstimatedDownloadLimit(false)) + "," + getShortString(getEstimatedDownloadLimit(true)) + "," + getShortString(getEstimatedDownloadCapacityBytesPerSec());
            log("Ping: rtt=" + rtt + ",x=" + x + ",y=" + y + ",m=" + metric + (new_region == null ? "" : (",region=" + new_region.getString())) + ",mr=" + getCurrentMetricRating() + ",up=[" + up_e + (best_good_up == null ? "" : (":" + getShortString(best_good_up))) + "],down=[" + down_e + (best_good_down == null ? "" : (":" + getShortString(best_good_down))) + "]" + ",bu=" + getLimitStr(last_bad_ups, true) + ",bd=" + getLimitStr(last_bad_downs, true));
        }
    }
