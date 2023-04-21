    @Test
    public void testValidateVerifyWithFile() throws Exception {
        CFDv3 cfd = new CFDv3(new FileInputStream("resources/xml/cfdv3_tfd.xml"));
        TFDv1 tfd = new TFDv1(cfd, pacCert);
        tfd.timbrar(pacKey);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        tfd.guardar(baos);
        CFDv3 cfd2 = new CFDv3(new ByteArrayInputStream(baos.toByteArray()));
        TFDv1 tfd2 = new TFDv1(cfd2, pacCert);
        tfd2.validar();
        tfd2.verificar();
    }
