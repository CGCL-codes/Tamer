    protected void drawLeafTask(int x, int y, int taskLen, int pgsLen, LayoutTask curTask) {
        if (log.isInfoEnabled()) {
            log.info("Paint task " + curTask + " at [" + x + "," + y + "], length is " + taskLen);
            try {
                Thread.sleep(DEBUG_PAUSE_TIME);
            } catch (InterruptedException ex) {
                log.error(ex.getLocalizedMessage());
            }
        }
        GanttColor taskBarBackcolor = curTask.getBackcolor() != null ? curTask.getBackcolor() : context.getConfig().getTaskBarBackColor();
        GanttRectangle taskRect = new GanttRectangle(x, y, (int) taskLen, task_bar_height);
        canvas.setColor(taskBarBackcolor, 255);
        canvas.fillRect(taskRect);
        if (pgsLen > 0) {
            GanttColor pgsBarColor = context.getConfig().getProgressBarBackColor();
            int pgsY = y + (task_bar_height - progress_bar_height) / 2;
            GanttRectangle pgsRect = new GanttRectangle(x, pgsY, (int) pgsLen, progress_bar_height);
            if (log.isDebugEnabled()) {
                log.debug(pgsRect, "Paint progress with progress " + pgsLen);
            }
            canvas.setColor(pgsBarColor);
            canvas.fillRect(pgsRect);
        }
        if (ArrayUtils.contains(this.context.getGanttModel().getSelectedIds(), curTask.getId())) {
            drawSelection(taskRect);
        }
        if (context.getConfig().isShowTaskInfoBehindTaskBar()) {
            String taskInfo = this.isDebug ? curTask.toSimpleString() : curTask.getName();
            int startX = x + (int) taskLen + 16;
            int actualY = y + 12;
            canvas.setColor(GanttColor.black);
            canvas.drawChars(taskInfo.toCharArray(), 0, taskInfo.length(), startX, actualY);
        }
        context.getTaskLocationManager().bindTaskAndShape(curTask, taskRect);
    }
