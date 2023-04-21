        public void accident(int i) {
            if (lock) {
                if (i > lockEnd && worker.db.isLocked()) {
                    try {
                        worker.log("releasing the lock");
                        worker.db.unlock();
                        worker.log("released the lock");
                    } catch (RequestException e) {
                        worker.log("unlock exception: " + e.getMessage());
                        fail("unlock exception: " + e.getMessage());
                    }
                }
            } else if (i > lockStart) {
                lock = true;
                String[] read = {};
                String[] write = { table };
                try {
                    worker.log("locking");
                    worker.db.lock(read, write);
                    worker.log("locked");
                } catch (RequestException e) {
                    worker.log("unlock exception: " + e.getMessage());
                    fail("unlock exception: " + e.getMessage());
                }
            }
        }
