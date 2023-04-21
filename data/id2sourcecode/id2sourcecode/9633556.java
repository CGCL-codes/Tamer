        public void run() {
            try {
                OutputStream os = s.getOutputStream();
                finished = 0;
                errors = 0;
                while (true) {
                    Thread.sleep(300);
                    if (percentage == -1 || (finished == 0 && errors == 0)) {
                        continue;
                    }
                    String report = System.currentTimeMillis() + " " + percentage + " " + finished + " " + errors + " " + outstanding + "\n";
                    String subreport = reads + " " + (((double) rlatency) / reads) + " " + writes + " " + (((double) wlatency / writes));
                    synchronized (statSync) {
                        finished = 0;
                        errors = 0;
                        reads = 0;
                        writes = 0;
                        rlatency = 0;
                        wlatency = 0;
                    }
                    os.write(report.getBytes());
                    System.out.println("Reporting " + report + "+" + subreport);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
