    protected void buildMeasurementGroups(Pool xmlPool, DevicePool pool) {
        for (MeasurementGroup measurementGroup : pool.getMeasurementGroups()) {
            es.cells.sardana.client.framework.config.MeasurementGroup xmlMeasurementGroup = xmlPool.addNewMeasurementGroup();
            xmlMeasurementGroup.setAlias(measurementGroup.getName());
            buildPollConfig(xmlMeasurementGroup, measurementGroup);
            buildLogging(xmlMeasurementGroup, measurementGroup);
            buildAttribute(xmlMeasurementGroup, measurementGroup, measurementGroup.getAttributeInfo("Timer"));
            buildAttribute(xmlMeasurementGroup, measurementGroup, measurementGroup.getAttributeInfo("Monitor"));
            buildAttribute(xmlMeasurementGroup, measurementGroup, measurementGroup.getAttributeInfo("Integration_time"));
            buildAttribute(xmlMeasurementGroup, measurementGroup, measurementGroup.getAttributeInfo("Integration_count"));
            for (SardanaDevice element : measurementGroup.getChannels()) {
                if (element instanceof CounterTimer) {
                    ReferenceType xmlCTExpChannelRef = xmlMeasurementGroup.addNewCTExpChannelRef();
                    xmlCTExpChannelRef.setName(element.getName());
                } else if (element instanceof ZeroDExpChannel) {
                    ReferenceType xmlZeroDExpChannelRef = xmlMeasurementGroup.addNewZeroDExpChannelRef();
                    xmlZeroDExpChannelRef.setName(element.getName());
                }
            }
        }
    }
