package com.fury.demos.workers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ImprovedWorkerDemo {

    private static final int NUMBER_OF_JOBS = 1000;

    private static final int WORKERS = Runtime.getRuntime().availableProcessors();

    private final CyclicBarrier startBarrier;

    private final BlockingQueue<Job> queue;

    private CyclicBarrier endBarrier;

    private long processStart;

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        new ImprovedWorkerDemo();
    }

    public ImprovedWorkerDemo() throws InterruptedException, BrokenBarrierException {
        queue = new ArrayBlockingQueue<Job>(NUMBER_OF_JOBS);
        Collection<Job> jobs = new ArrayList<Job>(NUMBER_OF_JOBS);
        for (int i = 0; NUMBER_OF_JOBS > i; i++) {
            jobs.add(new Job());
        }
        long start = System.nanoTime();
        for (Job job : jobs) {
            job.execute();
        }
        System.out.printf("1 thread takes %f ms.\n", (float) (System.nanoTime() - start) / 1000000f);
        startBarrier = new CyclicBarrier(WORKERS + 1);
        endBarrier = new CyclicBarrier(WORKERS + 1);
        Thread[] workThreads = new Thread[WORKERS];
        for (int i = 0; WORKERS > i; i++) {
            workThreads[i] = new Thread(new Worker());
            workThreads[i].start();
        }
        long lastTime = System.currentTimeMillis();
        long acc = 0;
        int fps = 0;
        int frames = 0;
        while (true) {
            long now = System.currentTimeMillis();
            acc += now - lastTime;
            frames++;
            if (acc >= 1000) {
                fps = frames;
                frames = 0;
                acc -= 1000;
                System.out.println("FPS: " + fps);
            }
            lastTime = now;
            processJobs(jobs);
        }
    }

    private void processJobs(Collection<Job> jobs) throws InterruptedException, BrokenBarrierException {
        processStart = System.nanoTime();
        long addingStart = System.nanoTime();
        queue.addAll(jobs);
        startBarrier.await();
        startBarrier.reset();
        endBarrier.await();
        endBarrier.reset();
    }

    private class Job {

        public void execute() {
            int a = 0;
            for (int i = 0; 1000 > i; i++) {
                a += i;
            }
        }
    }

    private class Worker implements Runnable {

        private static final int DRAINCOUNT = 100;

        private final Collection<Job> drainer;

        public Worker() {
            this.drainer = new ArrayList<Job>(DRAINCOUNT);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    startBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                long start = System.nanoTime();
                while (queue.drainTo(drainer, DRAINCOUNT) > 0) {
                    for (Job job : drainer) {
                        job.execute();
                    }
                    drainer.clear();
                }
                try {
                    endBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
