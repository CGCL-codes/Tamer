    private void persist() throws IOException {
        final LucenePersistor persistor = new LucenePersistor("index");
        final Collector collector = new Collector("file:/home/lucas/articles");
        collector.addResourceTypeToCollect("pdf");
        collector.addCollectorListener(new CollectorListener() {

            public void onStart() {
                System.out.println("Retriever started.");
                try {
                    persistor.open(true);
                } catch (final IOException e) {
                }
            }

            public void onCollect(final CollectorEvent event) {
                final List<Resource> collectedResources = event.getCollectedResources();
                for (Resource resource : collectedResources) {
                    try {
                        final Document document = resource.getData();
                        persistor.persist(new PersistentClass(document.getURL(), document.getContent()));
                    } catch (final Exception e) {
                    }
                }
            }

            public void onFinish() {
                try {
                    persistor.optimize();
                } catch (final IOException e) {
                }
                try {
                    persistor.close();
                } catch (final IOException e) {
                }
                System.out.println("Retriever done.");
            }

            public void onBrokenLink(final CollectorEvent collectorEvent) {
            }
        });
        collector.run();
    }
