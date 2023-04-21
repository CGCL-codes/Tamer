    public Result detect(File file) throws IOException {
        currentPosition = new Position();
        FileInputStream fstream = new FileInputStream(file);
        Result result = null;
        try {
            channel = fstream.getChannel();
            if (channel.size() == 0L) {
                result = new Result();
                result.setDescription("empty");
                result.setMime("application/x-empty");
                return result;
            }
            for (Pattern p : patterns) {
                result = process(result, p);
                if (result != null && !result.getDescription().isEmpty()) {
                    result.setDescription(result.getDescription().trim());
                    break;
                }
            }
        } finally {
            if (channel != null) {
                channel.close();
            }
        }
        return result;
    }
