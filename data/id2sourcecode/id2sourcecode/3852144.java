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
                int short_up = (int) upload_short_average.getAverage();
                int choke_speed = (int) choke_speed_average.getAverage();
                int choke_time = PING_CHOKE_TIME;
                int latency_factor = LATENCY_FACTOR;
                if (running_average < 2 * idle_average && ping_average < choke_time) {
                    direction = INCREASING;
                    int diff = running_average - idle_average;
                    if (diff < 100) {
                        diff = 100;
                    }
                    int increment = 1024 * (diff / latency_factor);
                    int max_inc = MAX_INCREMENT;
                    if (new_limit + 2 * 1024 > choke_speed) {
                        max_inc = 1024;
                    } else if (new_limit + 5 * 1024 > choke_speed) {
                        max_inc += 3 * 1024;
                    }
                    new_limit += Math.min(increment, max_inc);
                } else if (ping_average > 4 * idle_average || ping_average > choke_time) {
                    if (direction == INCREASING) {
                        if (idle_average_set) {
                            choke_speed_average.update(short_up);
                        }
                    }
                    direction = DECREASING;
                    int decrement = 1024 * ((ping_average - (3 * idle_average)) / latency_factor);
                    new_limit -= Math.min(decrement, MAX_DECREMENT);
                    if (new_limit < upload_short_prot_average.getAverage() + 1024) {
                        new_limit = (int) upload_short_prot_average.getAverage() + 1024;
                    }
                }
                if (new_limit < 1024) {
                    new_limit = 1024;
                }
            }
            int min_up = MIN_UP;
            int max_up = MAX_UP;
            if (min_up > 0 && new_limit < min_up && mode != MODE_FORCED_MIN) {
                new_limit = min_up;
            } else if (max_up > 0 && new_limit > max_up && mode != MODE_FORCED_MAX) {
                new_limit = max_up;
            }
            if (new_limit > current_limit && current_speed < (current_limit - 10 * 1024)) {
                new_limit = current_limit;
            }
        }
        new_limit = ((new_limit + 1023) / 1024) * 1024;
        adapter.setCurrentUploadLimit(new_limit);
        if (ADJUST_DOWNLOAD_ENABLE && !(Float.isInfinite(ADJUST_DOWNLOAD_RATIO) || Float.isNaN(ADJUST_DOWNLOAD_RATIO))) {
            int dl_limit = (int) (new_limit * ADJUST_DOWNLOAD_RATIO);
            adapter.setCurrentDownloadLimit(dl_limit);
        }
    }
