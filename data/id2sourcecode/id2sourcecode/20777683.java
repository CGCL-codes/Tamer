    @Test
    public void testConsoleEvents() throws InterruptedException, IOException {
        final ConsoleIoProvider ioProvider = createMock(ConsoleIoProvider.class);
        final EventProcessor eventProcessor = createMock(EventProcessor.class);
        final Injector injector = Guice.createInjector(Modules.override(new ConsoleModule()).with(new AbstractModule() {

            @Override
            protected void configure() {
                bind(ConsoleIoProvider.class).toInstance(ioProvider);
                bind(EventProcessor.class).toInstance(eventProcessor);
                bind(ConsoleConfig.class).toInstance(new ConsoleConfig());
            }
        }));
        final CallableTask console = injector.getInstance(ConsoleCommandExecutor.class);
        final Reader reader = createMock(Reader.class);
        final Writer writer = createMock(Writer.class);
        ioProvider.start();
        expect(ioProvider.getReader()).andReturn(reader).once();
        expect(ioProvider.getWriter()).andReturn(writer).once();
        for (final EventCase eventCase : eventCases) {
            expect(ioProvider.isInputValid()).andReturn(true).once();
            expect(reader.read(isA(char[].class), anyInt(), anyInt())).andAnswer(new IAnswer<Integer>() {

                public Integer answer() throws Throwable {
                    final Object[] objects = getCurrentArguments();
                    StringReader sr = new StringReader(eventCase.line);
                    sr.read((char[]) objects[0], (Integer) objects[1], (Integer) objects[2]);
                    return eventCase.line.length();
                }
            });
            eventProcessor.inject(isA(eventCase.evClass));
            expect(reader.ready()).andReturn(true).once();
        }
        expect(ioProvider.isInputValid()).andReturn(false).anyTimes();
        ioProvider.stop();
        replay(ioProvider, eventProcessor, reader, writer);
        try {
            console.call();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        verify(ioProvider, eventProcessor, reader, writer);
    }
