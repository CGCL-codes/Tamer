    private static void visitAllFiles(File folder, int option, PrintWriter writer) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                visitAllFiles(file, option, writer);
            } else {
                readFile(file, option, writer);
            }
        }
    }
