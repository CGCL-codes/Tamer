            public void run(IProgressMonitor monitor) {
                monitor.beginTask(Messages.OpenLogDialog_message, IProgressMonitor.UNKNOWN);
                try {
                    readLargeFile(writer);
                } catch (IOException e) {
                    writer.println(Messages.OpenLogDialog_cannotDisplay);
                }
            }
