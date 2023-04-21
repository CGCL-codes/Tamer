        public void run() {
            if (waitscreen != null) {
                int increment = 0;
                int progress = 0;
                increment = (20 / services.length) % 10;
                waitscreen.setPercent(0);
                Service services_new[] = new Service[services.length];
                if (pos1 == -1) {
                    for (int i = 0; i < services.length - 1; i++) {
                        services_new[i] = services[i + 1];
                        progress += increment;
                        waitscreen.setPercent(progress);
                        Thread.yield();
                    }
                    services_new[services.length - 1] = services[0];
                } else if (pos2 == -1) {
                    for (int i = 1; i < services.length; i++) {
                        services_new[i] = services[i - 1];
                        progress += increment;
                        waitscreen.setPercent(progress);
                        Thread.yield();
                    }
                    services_new[0] = services[services.length - 1];
                } else {
                    for (int i = 0; i < services.length; i++) {
                        services_new[i] = services[i];
                        if (i == pos1) {
                            services_new[i] = services[pos2];
                        } else if (i == pos2) {
                            services_new[i] = services[pos1];
                        }
                        progress += increment;
                        waitscreen.setPercent(progress);
                        Thread.yield();
                    }
                }
                formatServices();
                for (int i = (services_new.length - 1); i >= 0; i--) {
                    Service.insService(services_new[i].getString());
                    progress += (increment * 4);
                    waitscreen.setPercent(progress);
                    Thread.yield();
                }
                waitscreen.setPercent(Utils.WaitProgressScreen.PERCENT_MAX);
            }
        }
