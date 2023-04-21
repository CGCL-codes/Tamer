    public void process(DataTransferJob job) throws ProcessException {
        File f = new File(job.getJobProperties().getProperty("sourceFile"));
        FileInputStream is = null;
        ByteArrayOutputStream bos = null;
        try {
            is = new FileInputStream(f);
            bos = new ByteArrayOutputStream(512);
            copy(is, bos);
        } catch (Exception x) {
            throw new RuntimeException("!open file " + f.getAbsolutePath());
        } finally {
            close(bos);
            close(is);
        }
        job.setInputData(bos.toByteArray());
    }
