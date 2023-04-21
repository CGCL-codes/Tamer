            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                monitor.beginTask(PDERuntimeMessages.OpenLogDialog_message, IProgressMonitor.UNKNOWN);
                try {
                    readFile(writer);
                } catch (IOException e) {
                    writer.println(PDERuntimeMessages.OpenLogDialog_cannotDisplay);
                }
            }
