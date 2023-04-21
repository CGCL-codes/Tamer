    public void runTasks(float seconds, Object[] runners) {
        start = timer.getTimeInSeconds();
        end = start + seconds;
        currenttasks.clear();
        for (int k = 0; k < runners.length; k++) {
            Task[] tasks = ((TaskRunner) runners[k]).getTasks();
            for (int i = 0; i < tasks.length; i++) {
                Task task = tasks[i];
                if (task != null && task.isUsingManager(this)) {
                    if (task.isCompleted()) {
                        completedTask(task);
                    } else if (task.isPaused()) {
                        task.prepareRun();
                        currenttasks.add(tasks[i]);
                    }
                }
            }
        }
        for (int k = 0; k < runners.length; k++) {
            TaskRunner runner = (TaskRunner) runners[k];
            int tasksearch = tasklimit;
            int reserve = runner.reserveTask();
            while (reserve != -1 && --tasksearch >= 0) {
                if (tasks[tasksearch].isUsingRunner(runner)) {
                    Task task = tasks[tasksearch];
                    tasks[tasksearch] = null;
                    tasklimit--;
                    for (int i = tasksearch; i < tasklimit; i++) {
                        tasks[i] = tasks[i + 1];
                    }
                    task.assign(reserve);
                    task.prepareRun();
                    currenttasks.add(task);
                    reserve = runner.reserveTask();
                } else {
                }
            }
            if (reserve != -1) runner.cancelReserveTask(reserve);
        }
        Iterator it = currenttasks.iterator();
        while (it.hasNext() && timeLeft() > 0) {
            Task task = (Task) it.next();
            task.resume();
        }
        it = currenttasks.iterator();
        while (it.hasNext()) {
            Task task = (Task) it.next();
            task.attemptPause();
        }
    }
