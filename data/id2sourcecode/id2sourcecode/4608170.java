    @Override
    protected IStatus run(IProgressMonitor monitor) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(streamToRead));
            String line;
            while ((line = reader.readLine()) != null) out.write(line);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return Status.OK_STATUS;
    }
