    public void run() {
        double old_time = 0.;
        this.writer.writeLine(this.reader.readLine());
        String[] line = this.reader.readLine();
        while (line != null) {
            double time = Double.parseDouble(line[1]);
            if (time > 5 * 3600) {
                break;
            }
            for (PostProcessorI processor : this.processors) {
                line = processor.processEvent(line);
            }
            this.writer.writeLine(line);
            int min_id = 800000;
            if (this.floodlineGenerator != null && time > old_time) {
                old_time = time;
                Collection<FloodEvent> events = this.floodlineGenerator.getFlooded(time);
                int id = min_id;
                for (FloodEvent e : events) {
                    line[0] = Integer.toString(id);
                    int color = 0;
                    if (e.getFlooding() < MIN_WALKABLE) {
                        color = (int) (Math.min(20, e.getFlooding() / MIN_WALKABLE) * 20);
                    } else {
                        color = (int) (Math.min(235, e.getFlooding() / MAX_HEIGHT) * 235) + 20;
                    }
                    line[7] = Integer.toString(color);
                    line[9] = Integer.toString(id++);
                    line[11] = Double.toString(e.getX());
                    line[12] = Double.toString(e.getY());
                    line[15] = "-1";
                    this.writer.writeLine(line);
                }
            }
            line = this.reader.readLine();
        }
        this.writer.finish();
    }
