    @Override
    public void doJob() {
        BufferedReader reader = null;
        int line = 0;
        long size = 0;
        long consume = 0;
        long beg = System.currentTimeMillis();
        try {
            if (resource == null) {
                throw new java.lang.RuntimeException("jobWorker: " + workerName + " analysis resource is null");
            }
            if (resource.startsWith("file:")) {
                File file = new File(resource.substring("file:".length()));
                URL fileResource = null;
                if (!file.exists()) {
                    fileResource = ClassLoader.getSystemResource(resource.substring("file:".length()));
                    if (fileResource == null) throw new java.lang.RuntimeException("Job resource not exist,file : " + resource.substring("file:".length())); else logger.warn("load resource form classpath :" + fileResource.getFile());
                }
                if (fileResource == null) reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), config.getLogFileEncoding())); else reader = new BufferedReader(new InputStreamReader(fileResource.openStream(), config.getLogFileEncoding()));
            } else if (resource.startsWith("http:")) {
                URL url = new URL(resource);
                reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), config.getLogFileEncoding()));
            } else {
                throw new RuntimeException("resource must start with file: or http: ...");
            }
            String record;
            while ((record = reader.readLine()) != null) {
                if (record == null || "".equals(record)) continue;
                line += 1;
                size += record.length();
                String[] contents = record.split(splitRegex);
                Iterator<String> keys = entryPool.keySet().iterator();
                ReportEntry entry = null;
                List<ReportEntry> childEntrys = new ArrayList<ReportEntry>();
                Map<String, Object> valueTempPool = new HashMap<String, Object>();
                while (keys.hasNext()) {
                    try {
                        String key = keys.next();
                        entry = entryPool.get(key);
                        if (entry.isLazy()) continue;
                        if (entry.getParent() != null) {
                            childEntrys.add(entry);
                            continue;
                        }
                        process(entry, contents, valueTempPool);
                    } catch (Exception e) {
                        logger.error(new StringBuilder().append("Entry :").append(entry.getId()).append("\r\n record: ").append(record).toString(), e);
                        errorCounter.incrementAndGet();
                    }
                }
                ReportEntry reportEntry = null;
                for (Iterator<ReportEntry> iterator = childEntrys.iterator(); iterator.hasNext(); ) {
                    try {
                        reportEntry = iterator.next();
                        process(reportEntry, contents, valueTempPool);
                    } catch (Exception e) {
                        logger.error(new StringBuilder().append("Entry :").append(reportEntry.getId()).append("\r\n record: ").append(record).toString(), e);
                        errorCounter.incrementAndGet();
                    }
                }
            }
            if (line == 0) logger.error("there are no validate lines in this file..."); else logger.error(new StringBuilder("worker ").append(workerName).append(" process line count: ").append(line));
        } catch (Exception ex) {
            handleError(ex, null);
            this.setSuccess(false);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (Exception ex) {
                    handleError(ex, null);
                }
            }
            consume = System.currentTimeMillis() - beg;
            perf_logger.error(new StringBuilder().append("slave analysis,").append(line).append(",").append(size).append(",").append(consume).toString());
        }
    }
