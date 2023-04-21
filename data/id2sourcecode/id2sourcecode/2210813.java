    private void writeResultToFile(GridModel grid, int targetScore) {
        if (resultFileName != null) {
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                PrintWriter writer = new PrintWriter(new FileWriter(resultFileName));
                writer.println("#");
                writer.println(String.format("# Grid format: %dx%dx%d", grid.getSize(), grid.getSize(), grid.getPatternStats().getPatterns().size()));
                writer.println(String.format("#  Grid score: %d/%d", bestScore, targetScore));
                writer.println("#        Time: " + df.format(new Date()));
                writer.println("#     Elapsed: " + durationToString(getElapsedTime()));
                writer.println("#        Host: " + InetAddress.getLocalHost().getHostName());
                writer.println("#      Thread: " + Thread.currentThread().getName());
                writer.println("#");
                writer.println();
                writer.println(bestSolution.toQuadString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
