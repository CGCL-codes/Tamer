    void doBinarySearch() {
        callGC();
        int gCount = 0;
        int loops = 0;
        double dLoopCount = opt_loopCount * 3000 / (Math.log(tests.length) / Math.log(10) * tests.length);
        long startTime = 0;
        long elapsedTime = 0;
        if (opt_usekeys) {
            dLoopCount *= 5;
        }
        int adj_loopCount = (int) dLoopCount;
        if (adj_loopCount < 1) {
            adj_loopCount = 1;
        }
        for (; ; ) {
            if (opt_strcmp) {
                int r = 0;
                startTime = System.currentTimeMillis();
                for (loops = 0; loops < adj_loopCount; loops++) {
                    for (int j = 0; j < tests.length; j++) {
                        int hi = tests.length - 1;
                        int lo = 0;
                        int guess = -1;
                        for (; ; ) {
                            int newGuess = (hi + lo) / 2;
                            if (newGuess == guess) {
                                break;
                            }
                            guess = newGuess;
                            r = tests[j].compareTo(tests[guess]);
                            gCount++;
                            if (r == 0) {
                                break;
                            }
                            if (r < 0) {
                                hi = guess;
                            } else {
                                lo = guess;
                            }
                        }
                    }
                }
                elapsedTime = System.currentTimeMillis() - startTime;
                break;
            }
            if (opt_strcmpCPO) {
                int r = 0;
                startTime = System.currentTimeMillis();
                for (loops = 0; loops < adj_loopCount; loops++) {
                    for (int j = 0; j < tests.length; j++) {
                        int hi = tests.length - 1;
                        int lo = 0;
                        int guess = -1;
                        for (; ; ) {
                            int newGuess = (hi + lo) / 2;
                            if (newGuess == guess) {
                                break;
                            }
                            guess = newGuess;
                            r = com.ibm.icu.text.Normalizer.compare(tests[j], tests[guess], Normalizer.COMPARE_CODE_POINT_ORDER);
                            gCount++;
                            if (r == 0) {
                                break;
                            }
                            if (r < 0) {
                                hi = guess;
                            } else {
                                lo = guess;
                            }
                        }
                    }
                }
                elapsedTime = System.currentTimeMillis() - startTime;
                break;
            }
            if (opt_icu) {
                int r = 0;
                startTime = System.currentTimeMillis();
                for (loops = 0; loops < adj_loopCount; loops++) {
                    for (int j = 0; j < tests.length; j++) {
                        int hi = tests.length - 1;
                        int lo = 0;
                        int guess = -1;
                        for (; ; ) {
                            int newGuess = (hi + lo) / 2;
                            if (newGuess == guess) {
                                break;
                            }
                            guess = newGuess;
                            if (opt_usekeys) {
                                com.ibm.icu.text.CollationKey sortKey1 = icuCol.getCollationKey(tests[j]);
                                com.ibm.icu.text.CollationKey sortKey2 = icuCol.getCollationKey(tests[guess]);
                                r = sortKey1.compareTo(sortKey2);
                                gCount++;
                            } else {
                                r = icuCol.compare(tests[j], tests[guess]);
                                gCount++;
                            }
                            if (r == 0) {
                                break;
                            }
                            if (r < 0) {
                                hi = guess;
                            } else {
                                lo = guess;
                            }
                        }
                    }
                }
                elapsedTime = System.currentTimeMillis() - startTime;
                break;
            }
            if (opt_java) {
                int r = 0;
                startTime = System.currentTimeMillis();
                for (loops = 0; loops < adj_loopCount; loops++) {
                    for (int j = 0; j < tests.length; j++) {
                        int hi = tests.length - 1;
                        int lo = 0;
                        int guess = -1;
                        for (; ; ) {
                            int newGuess = (hi + lo) / 2;
                            if (newGuess == guess) {
                                break;
                            }
                            guess = newGuess;
                            if (opt_usekeys) {
                                java.text.CollationKey sortKey1 = javaCol.getCollationKey(tests[j]);
                                java.text.CollationKey sortKey2 = javaCol.getCollationKey(tests[guess]);
                                r = sortKey1.compareTo(sortKey2);
                                gCount++;
                            } else {
                                r = javaCol.compare(tests[j], tests[guess]);
                                gCount++;
                            }
                            if (r == 0) {
                                break;
                            }
                            if (r < 0) {
                                hi = guess;
                            } else {
                                lo = guess;
                            }
                        }
                    }
                }
                elapsedTime = System.currentTimeMillis() - startTime;
                break;
            }
            break;
        }
        int ns = (int) ((float) (1000000) * (float) elapsedTime / (float) gCount);
        if (!opt_terse) {
            System.out.println("binary search:  total # of string compares = " + gCount);
            System.out.println("binary search:  compares per loop = " + gCount / loops);
            System.out.println("binary search:  time per compare = " + ns);
        } else {
            System.out.println(ns);
        }
    }
