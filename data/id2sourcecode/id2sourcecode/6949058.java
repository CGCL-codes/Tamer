    public static void intergrateAapative(Vector initialState, Differential evaluator, float t_start, float t_end, float accuracy, Vector passback) {
        Real initial = Real.pool.aquire(initialState.size());
        Real end1 = Real.pool.aquire(initialState.size());
        Real end2 = Real.pool.aquire(initialState.size());
        Real mid = Real.pool.aquire(initialState.size());
        float t_mid = (t_start + t_end) / 2;
        initial.set(initialState);
        RK4.intergrate(initial, evaluator, t_start, t_end, end1);
        RK4.intergrate(initial, evaluator, t_start, t_mid, mid);
        RK4.intergrate(mid, evaluator, t_mid, t_end, end2);
        float error = 0;
        for (int i = 0; i < end1.size(); i++) {
            error = Math.max(Math.abs(end1.get(i) - end2.get(i)), error);
        }
        if (error < accuracy) {
            for (int i = 0; i < end2.size(); i++) {
                passback.set(i, end2.get(i));
            }
        } else {
            float stepsize = t_end - t_start;
            float maximumStepsize = stepsize * (float) Math.pow((error / accuracy), -1f / 4);
            int numberOfSteps = (int) Math.ceil(stepsize / maximumStepsize);
            float actualStepSize = stepsize / numberOfSteps;
            for (int i = 0; i < passback.size(); i++) {
                passback.set(i, initial.get(i));
            }
            float t_e = 0;
            float t_s = t_start;
            for (int i = 0; i < numberOfSteps; i++) {
                t_e = t_s + actualStepSize;
                RK4.intergrate(passback, evaluator, t_s, t_e, passback);
                t_s += actualStepSize;
                if (i / numberOfSteps == 2) {
                    mid.set(passback);
                }
            }
            if (!Real.epsilonEquals(t_e, t_end, .0001f)) {
                JOODELog.error("error, adaptive step did not correctly step to the end");
            }
        }
        Real.pool.release(initial);
        Real.pool.release(end1);
        Real.pool.release(end2);
        Real.pool.release(mid);
    }
