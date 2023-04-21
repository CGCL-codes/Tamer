    public Target<EvaluationResult> createTarget() throws Exception {
        if (output == null) return null;
        File f = new File(output);
        if (f.exists() && !overwrite) throw new Exception("Output already exists");
        return new StringFileTarget<EvaluationResult>(output);
    }
