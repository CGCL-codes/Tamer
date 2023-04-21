            public void run() {
                try {
                    System.out.println("starting write thread...");
                    for (int i = 0; i < NUM_INSERTS; i++) {
                        int db = (int) (Math.random() * numDBs);
                        byte[] key = randomBytes(minKeySize, maxKeySize);
                        byte[] val = randomBytes(minValSize, maxValSize);
                        int index = (int) (Math.random() * numIndices);
                        dbs[db].singleInsert(index, key, val, null).get();
                        if (i % 1000 == 0) {
                            System.out.print(".");
                            Thread.sleep(5000);
                        }
                        if (i % 10000 == 9999) {
                            System.out.println("\ncheckpoint...");
                            databaseSystem.getCheckpointer().checkpoint();
                            System.out.println("done");
                        }
                        if (i % 40000 == 39999) System.exit(0);
                    }
                    System.out.println("write thread finished");
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
