    @Test
    public void testTransform() throws Exception {
        final double min = 1.0;
        final double max = 2.0;
        final double total = 4.0;
        final long measurements = 1500;
        final String name = "measurement-name";
        final XmlAggregate aggregate = mock(XmlAggregate.class);
        when(aggregate.getMax()).thenReturn(max);
        when(aggregate.getMin()).thenReturn(min);
        when(aggregate.getTotal()).thenReturn(total);
        when(aggregate.getMeasurements()).thenReturn(measurements);
        when(aggregate.getName()).thenReturn(name);
        final StringWriter writer = new StringWriter();
        binder.bind(Collections.singletonList(aggregate), writer);
        final StringReader reader = new StringReader(writer.toString());
        final Collection<Aggregate> unmarshalledList = binder.unbind(reader);
        assertThat(unmarshalledList).hasSize(1);
        final Aggregate unmarshalled = unmarshalledList.iterator().next();
        assertThat(unmarshalled.getMax()).isEqualTo(max);
        assertThat(unmarshalled.getMin()).isEqualTo(min);
        assertThat(unmarshalled.getTotal()).isEqualTo(total);
        assertThat(unmarshalled.getMeasurements()).isEqualTo(measurements);
        assertThat(unmarshalled.getName()).isEqualTo(name);
    }
