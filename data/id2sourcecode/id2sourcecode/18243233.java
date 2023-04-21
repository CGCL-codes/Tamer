    public static void print(String str) {
        if (logging) {
            if (logFile != null || logFile.checkError()) {
                logFile.println(Timer.getTime() + ": " + str);
            } else {
                stopLog();
                print("Logging failed.");
            }
        }
        if (flushToStd) System.out.println(AppDefinition.getApplicationShortName() + " [" + Timer.getTime() + "]: " + str);
        if (!intiated) {
            return;
        }
        if (numberOfLinesEntered < maxVisibleLines) numberOfLinesEntered++;
        for (int i = 0; i < maxVisibleLines - 1; i++) text[i] = text[i + 1];
        text[maxVisibleLines - 1] = str;
        if (consoleState == State.HIDDEN && consoleAutoOpen) openConsole();
        if (consoleState != State.HIDDEN) {
            setTextArea();
            vertices[7] = area.getActualHeight() + 5.0f;
            vertices[10] = area.getActualHeight() + 5.0f;
        }
    }
