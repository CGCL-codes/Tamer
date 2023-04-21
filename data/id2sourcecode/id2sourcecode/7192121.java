    public void testRemoteExec2() throws IOException, JSchException, ConverterException {
        String command = ". .profile; /pica/tolk/bin/csfn_pica32norm -y | " + "/pica/tolk/bin/csfn_fcvnorm -k" + "FCV#pica#mab-ohne077 -t ALPHA |" + "/pica/tolk/bin/ddb_denorm -f mab-exchange |" + "/pica/tolk/bin/ddbflattenrecs -f";
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("LD_LIBRARY_PATH", "/pica/sybase/lib");
        properties.put("FILEMAP", "/pica/tolk/confdir/FILEMAP");
        RemoteExec remoteExec = new RemoteExec();
        remoteExec.setCommand(command);
        remoteExec.setHost("merkur.d-nb.de");
        remoteExec.setUser("tolk");
        remoteExec.setPassword("hads%szl");
        remoteExec.setEnvironmentProperties(properties);
        StringWriter writer = new StringWriter();
        Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream("test/input/02499250X.pp"), new PicaCharset()));
        remoteExec.remoteExec(reader, writer, new PicaCharset(), Charset.forName("UTF-8"), null, null);
        logger.info("output2: " + writer.toString());
    }
