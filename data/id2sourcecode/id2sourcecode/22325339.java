            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                monitor.beginTask(PDERuntimeMessages.OpenLogDialog_message, IProgressMonitor.UNKNOWN);
                try {
                    readLargeFile(writer);
                } catch (IOException e) {
                    writer.println(PDERuntimeMessages.OpenLogDialog_cannotDisplay);
                }
            }
