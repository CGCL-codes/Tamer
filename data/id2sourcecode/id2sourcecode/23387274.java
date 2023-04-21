    public void generateSamples(PerfMonSampleGenerator collector) throws IOException {
        String label = getLabel(isTranslate);
        switch(connector.getMetricType()) {
            case AgentConnector.PERFMON_CPU:
                collector.generateSample(100 * connector.getCpu(), label + ", %");
                break;
            case AgentConnector.PERFMON_MEM:
                collector.generateSample((double) connector.getMem() / PerfMonCollector.MEGABYTE, label + ", MB");
                break;
            case AgentConnector.PERFMON_SWAP:
                collector.generate2Samples(connector.getSwap(), label + " page in", label + " page out");
                break;
            case AgentConnector.PERFMON_DISKS_IO:
                collector.generate2Samples(connector.getDisksIO(), label + " reads", label + " writes");
                break;
            case AgentConnector.PERFMON_NETWORKS_IO:
                collector.generate2Samples(connector.getNetIO(), label + " recv, KB", label + " sent, KB", 1024d);
                break;
            default:
                throw new IOException("Unknown metric index: " + connector.getMetricType());
        }
    }
