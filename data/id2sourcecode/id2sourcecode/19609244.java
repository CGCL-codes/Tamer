            public void resultGroup(DHTSpeedTesterContact[] st_contacts, int[] round_trip_times) {
                if (!pm_enabled) {
                    for (int i = 0; i < st_contacts.length; i++) {
                        st_contacts[i].destroy();
                    }
                    return;
                }
                boolean sources_changed = false;
                for (int i = 0; i < st_contacts.length; i++) {
                    boolean found = false;
                    for (int j = 0; j < last_contact_group.length; j++) {
                        if (st_contacts[i] == last_contact_group[j]) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        sources_changed = true;
                        break;
                    }
                }
                last_contact_group = st_contacts;
                pingContact[] sources = new pingContact[st_contacts.length];
                boolean miss = false;
                int worst_value = -1;
                int min_value = Integer.MAX_VALUE;
                int num_values = 0;
                int total = 0;
                synchronized (contacts) {
                    for (int i = 0; i < st_contacts.length; i++) {
                        pingContact source = sources[i] = (pingContact) contacts.get(st_contacts[i]);
                        if (source != null) {
                            int rtt = round_trip_times[i];
                            if (rtt >= 0) {
                                if (rtt > worst_value) {
                                    worst_value = rtt;
                                }
                                if (rtt < min_value) {
                                    min_value = rtt;
                                }
                                num_values++;
                                total += rtt;
                            }
                            source.setPingTime(rtt);
                        } else {
                            miss = true;
                        }
                    }
                }
                if (miss) {
                    Debug.out("Auto-speed: source missing");
                } else {
                    provider.calculate(sources);
                    if (num_values > 1) {
                        total -= worst_value;
                        num_values--;
                    }
                    if (num_values > 0) {
                        int average = total / num_values;
                        average = (average + min_value) / 2;
                        addPingHistory(average, sources_changed);
                    }
                }
            }
