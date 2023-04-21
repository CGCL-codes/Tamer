    public void logMessage(String message) {
        if (pos == 6) {
            for (int i = 0; i < 5; i++) {
                lines[i] = lines[i + 1];
            }
            lines[5] = message;
        } else {
            lines[pos] = message;
            pos++;
        }
        canvas.update(canvas.getGraphics());
    }
