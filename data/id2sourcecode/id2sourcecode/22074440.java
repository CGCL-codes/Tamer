    @Test
    public void testXmlBinding() {
        final AggregateBinder binder = new XmlAggregateBinder();
        final StringWriter writer = new StringWriter();
        final BindingMeasurementRenderer renderer = new BindingMeasurementRenderer(binder, writer);
        BasicEtmConfigurator.configure();
        final EtmMonitor monitor = EtmManager.getEtmMonitor();
        monitor.start();
        final String pointAName = "monitor.a.point";
        final EtmPoint pointA = monitor.createPoint(pointAName);
        pointA.collect();
        final String pointBName = "monitor.b.point";
        final EtmPoint pointB = monitor.createPoint(pointBName);
        pointB.collect();
        monitor.stop();
        monitor.render(renderer);
        final StringReader reader = new StringReader(writer.toString());
        final Collection<Aggregate> aggregates = binder.unbind(reader);
        boolean hasA = false;
        boolean hasB = false;
        for (Aggregate aggregate : aggregates) {
            hasA |= pointAName.equals(aggregate.getName());
            hasB |= pointBName.equals(aggregate.getName());
        }
        assertThat(hasA).as("Point A not found in data: " + aggregates).isTrue();
        assertThat(hasB).as("Point B not found in data: " + aggregates).isTrue();
    }
