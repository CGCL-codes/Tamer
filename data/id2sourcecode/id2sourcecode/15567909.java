    @Test
    public void recordAndVerifyWithMixedCascadeLevels(@Cascading final SocketFactory sf) throws Exception {
        new NonStrictExpectations() {

            {
                sf.createSocket("first", 80).getKeepAlive();
                result = true;
                sf.createSocket("second", anyInt).getChannel().close();
                times = 1;
            }
        };
        sf.createSocket("second", 80).getChannel().close();
        assertTrue(sf.createSocket("first", 80).getKeepAlive());
        sf.createSocket("first", 8080).getChannel().provider().openPipe();
        new Verifications() {

            {
                sf.createSocket("first", 8080).getChannel().provider().openPipe();
            }
        };
    }
