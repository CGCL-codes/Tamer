    public synchronized void readOneStep() {
        if (finishedReading) return;
        double actTime = time;
        if (readVehicle == null) {
            readOneLine();
            writer.positions.add(readVehicle);
            actTime = time;
        } else {
            writer.positions.clear();
            writer.positions.add(readVehicle);
        }
        while (readOneLine() && time == actTime) writer.positions.add(readVehicle);
        if (time == actTime) finishedReading = true;
        synchronized (buf) {
            buf.position(0);
            quad.writeDynData(null, buf);
            byte[] buffer = new byte[buf.position() + 1];
            System.arraycopy(buf.array(), 0, buffer, 0, buffer.length);
            nextTime = actTime;
            timesteps.put((int) nextTime, buffer);
            actBuffer = buffer;
        }
    }
