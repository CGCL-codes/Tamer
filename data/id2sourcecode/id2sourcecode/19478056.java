    public void calculate(SpeedManagerPingSource[] sources) {
        int min_rtt = UNLIMITED;
        for (int i = 0; i < sources.length; i++) {
            int rtt = sources[i].getPingTime();
            if (rtt >= 0 && rtt < min_rtt) {
                min_rtt = rtt;
            }
        }
        String str = "";
        int ping_total = 0;
        int ping_count = 0;
        for (int i = 0; i < sources.length; i++) {
            pingSource ps;
            synchronized (ping_sources) {
                ps = (pingSource) ping_sources.get(sources[i]);
            }
            int rtt = sources[i].getPingTime();
            str += (i == 0 ? "" : ",") + rtt;
            if (ps != null) {
                boolean good_ping = rtt < 5 * Math.max(min_rtt, 75);
                ps.pingReceived(rtt, good_ping);
                if (!good_ping) {
                    rtt = -1;
                }
            }
            if (rtt != -1) {
                ping_total += rtt;
                ping_count++;
            }
        }
        if (ping_count == 0) {
            return;
        }
        int ping_average = ping_total / ping_count;
        ping_average = (ping_average + min_rtt) / 2;
        int running_average = (int) ping_average_history.update(ping_average);
        if (ping_average > max_ping) {
            max_ping = ping_average;
        }
        int up_average = (int) upload_average.getAverage();
        if (up_average <= IDLE_UPLOAD_SPEED || (running_average < idle_average && !idle_average_set)) {
            idle_ticks++;
            if (idle_ticks >= PING_AVERAGE_HISTORY_COUNT) {
                idle_average = Math.max(running_average, MIN_IDLE_AVERAGE);
                log("New idle average: " + idle_average);
                idle_average_set = true;
            }
        } else {
            if (up_average > max_upload_average) {
                max_upload_average = up_average;
                log("New max upload:" + max_upload_average);
            }
            idle_ticks = 0;
        }
        if (idle_average_set && running_average < idle_average) {
            idle_average = Math.max(running_average, MIN_IDLE_AVERAGE);
        }
        int current_speed = adapter.getCurrentDataUploadSpeed() + adapter.getCurrentProtocolUploadSpeed();
        int current_limit = adapter.getCurrentUploadLimit();
        int new_limit = current_limit;
        log("Pings: " + str + ", average=" + ping_average + ", running_average=" + running_average + ",idle_average=" + idle_average + ", speed=" + current_speed + ",limit=" + current_limit + ",choke = " + (int) choke_speed_average.getAverage());
        if (mode == MODE_FORCED_MAX) {
            if (mode_ticks > FORCED_MAX_TICKS) {
                mode = MODE_RUNNING;
                current_limit = new_limit = saved_limit;
            }
        } else if (mode == MODE_FORCED_MIN) {
            if (idle_average_set || mode_ticks > FORCED_MIN_TICKS) {
                log("Mode -> running");
                if (!idle_average_set) {
                    idle_average = Math.max(running_average, MIN_IDLE_AVERAGE);
                    idle_average_set = true;
                }
                mode = MODE_RUNNING;
                mode_ticks = 0;
                current_limit = new_limit = saved_limit;
            } else if (mode_ticks == 5) {
                ping_average_history.reset();
            }
        }
        if (mode == MODE_RUNNING) {
            if ((ticks > FORCED_MIN_AT_START_TICK_LIMIT && !idle_average_set) || (replacement_contacts >= 2 && idle_average_set)) {
                log("Mode -> forced min");
                mode = MODE_FORCED_MIN;
                mode_ticks = 0;
                saved_limit = current_limit;
                idle_average_set = false;
                idle_ticks = 0;
                replacement_contacts = 0;
                new_limit = FORCED_MIN_SPEED;
            } else {
                limiter.setDlSpeed(adapter.getCurrentDataDownloadSpeed());
                limiter.setUlSpeed(adapter.getCurrentDataUploadSpeed());
                limiter.setMaxDlSpeed(adapter.getSpeedManager().getEstimatedDownloadCapacityBytesPerSec().getBytesPerSec());
                limiter.setMaxUlSpeed(adapter.getSpeedManager().getEstimatedUploadCapacityBytesPerSec().getBytesPerSec());
                limiter.setLatency(ping_average);
                limiter.setMinLatency(idle_average);
                limiter.setMaxLatency(1500);
                if (limiter.shouldLimitDownload()) {
                    adapter.setCurrentDownloadLimit((int) limiter.getDownloadLimit());
                } else {
                    adapter.setCurrentDownloadLimit(0);
                }
                if (limiter.shouldLimitUpload()) {
                    adapter.setCurrentUploadLimit((int) limiter.getUploadLimit());
                } else {
                    adapter.setCurrentUploadLimit(0);
                }
            }
        }
    }
