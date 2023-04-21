                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("ektorp-doc-writer-thread-%s", threadCount.incrementAndGet()));
                }
