    public static void writeIntroductoryInfo(AGEClientWindow w) {
        w.write("Aetheria Game Engine v " + UIMessages.getInstance().getMessage("age.version") + "\n");
        w.write(UIMessages.getInstance().getMessage("age.copyright") + "\n");
        w.write(UIMessages.getInstance().getMessage("intro.legal") + "\n");
        w.write("\n=== === === === === === === === === === === === === === === ===");
        w.write("\n" + w.getIO().getColorCode("information") + "Engine-related Version Info:");
        w.write("\n" + w.getIO().getColorCode("information") + "[OS Layer]           " + System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch") + w.getIO().getColorCode("reset"));
        w.write("\n" + w.getIO().getColorCode("information") + "[Java Layer]         " + System.getProperty("java.version") + w.getIO().getColorCode("reset"));
        w.write("\n" + w.getIO().getColorCode("information") + "[Simulation Layer]   " + GameEngineThread.getVersion() + w.getIO().getColorCode("reset"));
        w.write("\n" + w.getIO().getColorCode("information") + "[Object Code Layer]  " + ObjectCode.getInterpreterVersion() + w.getIO().getColorCode("reset"));
        w.write("\n" + w.getIO().getColorCode("information") + "[UI Layer]           " + w.getVersion() + w.getIO().getColorCode("reset"));
        w.write("\n=== === === === === === === === === === === === === === === ===\n\n");
    }
