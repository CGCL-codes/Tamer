            @Override
            public void run() {
                try {
                    Thread.currentThread().setName("Async2-Thread");
                    log.info("Putting AsyncThread to sleep");
                    Thread.sleep(2 * 1000);
                    log.info("Writing data.");
                    actx.getResponse().getWriter().write("Output from background thread. Time:" + System.currentTimeMillis() + "\n");
                    actx.complete();
                } catch (InterruptedException x) {
                    log.error("Async2", x);
                } catch (IllegalStateException x) {
                    log.error("Async2", x);
                } catch (IOException x) {
                    log.error("Async2", x);
                }
            }
