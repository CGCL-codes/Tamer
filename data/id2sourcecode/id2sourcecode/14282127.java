    public void showError(String command, Process p) {
        try {
            StringWriter errorWriter = new StringWriter();
            BufferedWriter bw = new BufferedWriter(errorWriter);
            bw.write(Pooka.getProperty("ExternalLauncher.error.failedToRun", "Failed executing command:"));
            bw.newLine();
            bw.write(command);
            bw.newLine();
            bw.newLine();
            bw.write(Pooka.getProperty("ExternalLauncher.error.output", "Output:"));
            bw.newLine();
            try {
                InputStream errorStream = p.getErrorStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(errorStream));
                for (String nextLine = br.readLine(); nextLine != null; nextLine = br.readLine()) {
                    bw.write(nextLine);
                    bw.newLine();
                }
            } catch (IOException ioe) {
                bw.write("Error not available");
                bw.newLine();
            }
            bw.flush();
            bw.close();
            String errorMessage = errorWriter.toString();
            Pooka.getUIFactory().showError(errorMessage);
        } catch (IOException ioe) {
            Pooka.getUIFactory().showError(Pooka.getProperty("ExternalLauncher.error.failedToRun", "Failed executing command:"));
        }
    }
