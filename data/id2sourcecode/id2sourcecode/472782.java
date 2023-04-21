    public void run() {
        long startTime, endTime;
        try {
            init(options);
            URL url = new URL(new URL("file:."), file);
            String chars = null;
            byte[] bytes = null;
            switch(inputProcessing) {
                case PREDECODE:
                    chars = slurpFileIntoString(file);
                    break;
                case PRELOAD:
                    bytes = slurpFileIntoBytes(file);
                    break;
            }
            if (iterations == 0) {
                switch(inputProcessing) {
                    case PREDECODE:
                        parse(new StringReader(chars));
                        break;
                    case PRELOAD:
                        parse(new ByteArrayInputStream(bytes));
                        break;
                    default:
                        parse(url.openStream());
                }
                info("Parsed once for a dry run");
                return;
            }
            info("Warming up the parser....");
            startTime = System.currentTimeMillis();
            int count = 0;
            while (System.currentTimeMillis() - startTime < 5000) {
                switch(inputProcessing) {
                    case PREDECODE:
                        parse(new StringReader(chars));
                        break;
                    case PRELOAD:
                        parse(new ByteArrayInputStream(bytes));
                        break;
                    default:
                        parse(url.openStream());
                }
                ++count;
            }
            info("warm-up count=" + count);
            info("Parsing " + file + " " + iterations + " times by " + getClass().getName());
            startTime = System.currentTimeMillis();
            switch(inputProcessing) {
                case PREDECODE:
                    startTime = System.currentTimeMillis();
                    for (int i = 0; i < iterations; i++) parse(new StringReader(chars));
                    endTime = System.currentTimeMillis();
                    break;
                case PRELOAD:
                    startTime = System.currentTimeMillis();
                    for (int i = 0; i < iterations; i++) parse(new ByteArrayInputStream(bytes));
                    endTime = System.currentTimeMillis();
                    break;
                default:
                    startTime = System.currentTimeMillis();
                    for (int i = 0; i < iterations; i++) parse(url.openStream());
                    endTime = System.currentTimeMillis();
            }
            info("Elapsed time: " + (endTime - startTime) + "ms");
            info("Average parse time: " + ((float) (endTime - startTime) / iterations) + "ms");
            msg("<benchmark elapsed=\"" + (endTime - startTime) + "\" iterations=\"" + iterations + "\"/>");
        } catch (Exception e) {
            msg("<error>" + e.toString() + "</error>");
            e.printStackTrace();
        }
    }
