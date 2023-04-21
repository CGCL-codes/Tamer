    public static String readNextTelemetryState() {
        try {
            System.out.println("Reading next waypoint from the file");
            nextLine = telemetryStream.readLine();
            if (nextLine == null) {
                System.out.println("Entering if loop for nextLine == null");
                TelemetryFileClose();
                TelemetryFileOpen();
                nextLine = readNextTelemetryState();
            }
            return nextLine;
        } catch (EOFException eof) {
            System.err.println("End of waypoint file, reopen\n" + eof.toString());
            TelemetryFileClose();
            TelemetryFileOpen();
            readNextTelemetryState();
        } catch (IOException e) {
            System.err.println("Error during read from file\n" + e.toString());
            System.exit(1);
        }
        return "";
    }
