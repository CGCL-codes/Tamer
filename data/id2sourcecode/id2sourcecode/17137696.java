                public void generate(IndentWriter writer) {
                    writer.println("Thread Pools");
                    try {
                        writer.indent();
                        List pools;
                        synchronized (busy_pools) {
                            pools = new ArrayList(busy_pools);
                        }
                        for (int i = 0; i < pools.size(); i++) {
                            ((ThreadPool) pools.get(i)).generateEvidence(writer);
                        }
                    } finally {
                        writer.exdent();
                    }
                }
