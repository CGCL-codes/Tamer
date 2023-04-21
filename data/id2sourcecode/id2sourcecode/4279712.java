        private void doTest(int min, int max) {
            if (min == max) {
                output("padding required: " + min, "lime");
                pass();
                stop();
            } else {
                this.min = min;
                this.max = max;
                this.padding = (min + max) / 2;
                output("padding test: " + min + " " + max + " " + padding, "silver");
                String url = GWT.getModuleBaseURL() + "connection?delay=60000&padding=" + padding;
                doStart(url, null);
            }
        }
