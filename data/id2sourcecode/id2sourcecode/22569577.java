    protected List<String> getReportedModelIDs() {
        List<String> targets = null;
        Properties cfg = new Properties();
        try {
            URL url = this.getClass().getClassLoader().getResource(CFG_FILE_NAME);
            cfg.load(url.openStream());
            String ts = cfg.getProperty(KEY_TARGETS);
            if (ts != null) {
                targets = Arrays.asList(cfg.getProperty(KEY_TARGETS).split(","));
            } else {
                String last = cfg.getProperty(KEY_SHOW_LAST);
                if (last == null) {
                    return null;
                }
                int n = Integer.valueOf(last).intValue();
                try {
                    List<Model> models = DaoUtil.getModelDao().getAll();
                    int sz = (models == null ? 0 : models.size());
                    if (sz > 0) {
                        targets = new ArrayList<String>(sz);
                        for (int i = 0; i < n; i++) {
                            targets.add(0, String.valueOf(sz - i));
                        }
                    }
                } catch (BeanPersistenceException e) {
                    e.printStackTrace();
                } catch (StaleDataException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return targets;
    }
