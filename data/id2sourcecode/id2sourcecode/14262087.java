    public WriteController() {
        Thread write_processor_thread = new AEThread("WriteController:WriteProcessor") {

            public void runSupport() {
                writeProcessorLoop();
            }
        };
        write_processor_thread.setDaemon(true);
        write_processor_thread.setPriority(Thread.MAX_PRIORITY - 1);
        write_processor_thread.start();
        Set types = new HashSet();
        types.add(AzureusCoreStats.ST_NET_WRITE_CONTROL_WAIT_COUNT);
        types.add(AzureusCoreStats.ST_NET_WRITE_CONTROL_NP_COUNT);
        types.add(AzureusCoreStats.ST_NET_WRITE_CONTROL_P_COUNT);
        types.add(AzureusCoreStats.ST_NET_WRITE_CONTROL_ENTITY_COUNT);
        types.add(AzureusCoreStats.ST_NET_WRITE_CONTROL_CON_COUNT);
        types.add(AzureusCoreStats.ST_NET_WRITE_CONTROL_READY_CON_COUNT);
        types.add(AzureusCoreStats.ST_NET_WRITE_CONTROL_READY_BYTE_COUNT);
        AzureusCoreStats.registerProvider(types, this);
        AEDiagnostics.addEvidenceGenerator(new AEDiagnosticsEvidenceGenerator() {

            public void generate(IndentWriter writer) {
                writer.println("Write Controller");
                try {
                    writer.indent();
                    ArrayList ref = normal_priority_entities;
                    writer.println("normal - " + ref.size());
                    for (int i = 0; i < ref.size(); i++) {
                        RateControlledEntity entity = (RateControlledEntity) ref.get(i);
                        writer.println(entity.getString());
                    }
                    ref = boosted_priority_entities;
                    writer.println("boosted - " + ref.size());
                    for (int i = 0; i < ref.size(); i++) {
                        RateControlledEntity entity = (RateControlledEntity) ref.get(i);
                        writer.println(entity.getString());
                    }
                    ref = high_priority_entities;
                    writer.println("priority - " + ref.size());
                    for (int i = 0; i < ref.size(); i++) {
                        RateControlledEntity entity = (RateControlledEntity) ref.get(i);
                        writer.println(entity.getString());
                    }
                } finally {
                    writer.exdent();
                }
            }
        });
    }
