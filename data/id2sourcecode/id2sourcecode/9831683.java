    public void testEscrituraFichero() {
        try {
            FileUtils.copyFile(new File(SRC_TEST_CONFIG, "pom_testEscrituraFichero.xml"), new File(SRC_TEST_CONFIG, "pom.xml"));
        } catch (IOException e) {
            fail("No se puede copiar fichero " + e);
        }
        Artifact artifactOrigen = new Artifact();
        artifactOrigen.setGroupId("com.chuidiang");
        artifactOrigen.setArtifactId("pom_version");
        artifactOrigen.setVersion("1.1.0");
        Artifact artifactDestino = new Artifact();
        artifactDestino.setGroupId("com.chuidiang");
        artifactDestino.setArtifactId("pom_version");
        artifactDestino.setVersion("1.1.1");
        Hashtable<Artifact, Artifact> cambios = new Hashtable<Artifact, Artifact>();
        cambios.put(artifactOrigen, artifactDestino);
        new CambiaPom(cambios, new File(SRC_TEST_CONFIG), false, new SystemStreamLog(), null);
        File pom = new File(SRC_TEST_CONFIG, "pom.xml");
        File pom1 = new File(SRC_TEST_CONFIG, "pom1.xml");
        assertTrue(pom.exists());
        assertTrue(pom1.exists());
        try {
            BufferedReader brentrada = new BufferedReader(new FileReader(pom));
            BufferedReader bsalida = new BufferedReader(new FileReader(pom1));
            String lineaEntrada = avanzaHastaPrimerGroupId(brentrada);
            String lineaEntrada2 = avanzaHastaPrimerGroupId(bsalida);
            while (null != lineaEntrada) {
                if (lineaEntrada.indexOf("1.1.1") == -1) {
                    assertEquals(lineaEntrada, lineaEntrada2);
                } else {
                    assertEquals("<version>1.1.1</version>", lineaEntrada.trim());
                    assertEquals("<version>1.1.0</version>", lineaEntrada2.trim());
                }
                lineaEntrada = brentrada.readLine();
                lineaEntrada2 = bsalida.readLine();
            }
            assertNull(lineaEntrada);
            assertNull(lineaEntrada2);
            brentrada.close();
            bsalida.close();
        } catch (Exception e) {
            fail("no se puede leer fichero " + e);
        }
    }
