    private synchronized void readOneStep() {
        if (this.finishedReading) return;
        double actTime = this.time;
        if (this.readVehicle == null) {
            readOneLine();
            this.writer.positions.add(this.readVehicle);
            actTime = this.time;
        } else {
            this.writer.positions.clear();
            this.writer.positions.add(this.readVehicle);
        }
        while (readOneLine() && (this.time == actTime)) this.writer.positions.add(this.readVehicle);
        if (this.time == actTime) this.finishedReading = true;
        synchronized (this.buf) {
            this.buf.position(0);
            this.quad.writeDynData(null, this.buf);
            byte[] buffer = new byte[this.buf.position() + 1];
            System.arraycopy(this.buf.array(), 0, buffer, 0, buffer.length);
            this.nextTime = actTime;
            this.timesteps.put((int) this.nextTime, buffer);
        }
    }
