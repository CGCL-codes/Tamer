        @Override
        protected void compute() {
            final Thread thread = Thread.currentThread();
            ForkJoinThread forkJoinThread = threadMap.get(thread);
            if (forkJoinThread == null) {
                forkJoinThread = new ForkJoinThread(thread);
                threadMap.put(thread, forkJoinThread);
            }
            ForkJoinSprite sprite = new ForkJoinSprite(start, end, level);
            forkJoinThread.setCurrentSprite(sprite);
            canvas.addSprite(sprite);
            bumpThreadCount(thread);
            final int length = end - start;
            if (length == 1) {
                result = array[start];
                concurrentExample.setState(2);
                sleep(.75f);
            } else {
                concurrentExample.setState(3);
                sleep(1);
                int mid = (start + end) / 2;
                forkJoinThread.setCurrentSprite(null);
                Solver solver1 = new Solver(array, start, mid, level + 1);
                Solver solver2 = new Solver(array, mid, end, level + 1);
                invokeAll(solver1, solver2);
                forkJoinThread.setCurrentSprite(sprite);
                result = Math.max(solver1.result, solver2.result);
            }
            sleep(.75f);
            sprite.setComplete(result);
        }
