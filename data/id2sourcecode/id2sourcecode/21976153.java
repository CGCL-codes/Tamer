    public void doPoint(double leftX, double leftY, double rightX, double rightY, int ypos, int xpos, int stepY, int stepX, int correctX, int correctY) {
        double middleX = (leftX + rightX) / 2, middleY = (leftY + rightY) / 2;
        IterationResult result = new BasinLogic(basinController).iterate(app, executer, middleX, middleY);
        logger.debug("Iteration result: " + middleX + "," + middleY + " :: " + Arrays.toString(result.getAverages()));
        Color color = basinController.getPointColor(result.getAverages());
        graphics.setColor(color);
        graphics.fillRect(xpos, height - ypos - stepY, stepX, stepY);
        this.color[correctY][correctX] = color;
    }
