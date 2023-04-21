    public void setSpeedTester(DHTSpeedTester _tester) {
        if (speed_tester != null) {
            if (!emulated_ping_source) {
                Debug.out("speed tester already set!");
            }
            return;
        }
        COConfigurationManager.setParameter(CONFIG_AVAIL, true);
        speed_tester = _tester;
        speed_tester.addListener(new DHTSpeedTesterListener() {

            private DHTSpeedTesterContact[] last_contact_group = new DHTSpeedTesterContact[0];

            public void contactAdded(DHTSpeedTesterContact contact) {
                if (core.getInstanceManager().isLANAddress(contact.getAddress().getAddress())) {
                    contact.destroy();
                } else {
                    log("activePing: " + contact.getString());
                    contact.setPingPeriod(CONTACT_PING_SECS);
                    synchronized (contacts) {
                        pingContact source = new pingContact(contact);
                        contacts.put(contact, source);
                        contacts_array = new pingContact[contacts.size()];
                        contacts.values().toArray(contacts_array);
                        total_contacts++;
                        provider.pingSourceFound(source, total_contacts > CONTACT_NUMBER);
                    }
                    contact.addListener(new DHTSpeedTesterContactListener() {

                        public void ping(DHTSpeedTesterContact contact, int round_trip_time) {
                        }

                        public void pingFailed(DHTSpeedTesterContact contact) {
                        }

                        public void contactDied(DHTSpeedTesterContact contact) {
                            log("deadPing: " + contact.getString());
                            synchronized (contacts) {
                                pingContact source = (pingContact) contacts.remove(contact);
                                if (source != null) {
                                    contacts_array = new pingContact[contacts.size()];
                                    contacts.values().toArray(contacts_array);
                                    provider.pingSourceFailed(source);
                                }
                            }
                        }
                    });
                }
            }

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
        });
        if (pm_enabled) {
            speed_tester.setContactNumber(CONTACT_NUMBER);
        }
        SimpleTimer.addPeriodicEvent("SpeedManager:stats", SpeedManagerAlgorithmProvider.UPDATE_PERIOD_MILLIS, new TimerEventPerformer() {

            public void perform(TimerEvent event) {
                if (enabled) {
                    provider.updateStats();
                }
            }
        });
    }
