        private void subscribe() {
            MetricListener ml = this;
            try {
                this.initFile(jobId);
                MonitorConsumer mc = new MonitorConsumer(this.monitorURL);
                mc.addMetricListener(ml);
                mc.auth();
                MonitorArg args[] = new MonitorArg[1];
                args[0] = new MonitorArg("jobid", jobId);
                MonitorConsumer.CollectResult cr = (MonitorConsumer.CollectResult) mc.collect("application.message", args);
                cr.waitResult();
                MonitorConsumer.MetricInstance suscribeMetric = cr.getMetricInstance();
                int mid = suscribeMetric.getMetricId();
                this.metricId = mid;
                int channel = mc.getChannelId();
                System.out.println("\n\tTestClient.subscibe(" + this.monitorURL + ", " + jobId + ") - mid:" + mid);
                MonitorConsumer.CommandResult sr = mc.subscribe(mid, channel);
                sr.waitResult();
                int status = sr.getStatus();
                if (status != 0) {
                    System.out.println("\n\tTestClient.subscribe(" + this.monitorURL + ", " + jobId + ") -Suscribe failed: " + sr.getStatusStr());
                    System.exit(1);
                }
                System.out.println("\n\tTestClient.subscibe(" + this.monitorURL + ", " + jobId + ") - Subscribe SUCCESSFUL. Waiting for data...");
                while (true) {
                    try {
                        java.lang.Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
            } catch (MonitorException mex) {
                System.out.println("\n\tTestClient.subscribe(" + ml + "," + this.monitorURL + ", " + this.jobId + ") -- FAILED.");
                mex.printStackTrace();
            }
        }
