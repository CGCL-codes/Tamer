    public void testCambiaPomConVariables() {
        boolean encontradaVariable = false;
        try {
            FileUtils.copyFile(new File(SRC_TEST_CONFIG, "pom_conVariables.xml"), new File(SRC_TEST_CONFIG, "pom.xml"));
        } catch (IOException e) {
            fail("No se puede copiar fichero " + e);
        }
        Properties propiedades = new Properties();
        propiedades.setProperty("variableExiste", "1.2.3");
        Hashtable<Artifact, Artifact> cambios = new Hashtable<Artifact, Artifact>();
        Artifact artifactOriginal = new Artifact();
        artifactOriginal.setGroupId("org.apache.maven");
        artifactOriginal.setArtifactId("maven-plugin-api");
        artifactOriginal.setVersion("1.2.3");
        Artifact artifactDestino = new Artifact();
        artifactDestino.setGroupId("org.apache.maven");
        artifactDestino.setArtifactId("maven-plugin-api");
        artifactDestino.setVersion("3.2.1");
        cambios.put(artifactOriginal, artifactDestino);
        new CambiaPom(cambios, new File(SRC_TEST_CONFIG), true, new SystemStreamLog(), propiedades);
        File pom = new File(SRC_TEST_CONFIG, "pom.xml");
        File pom1 = new File(SRC_TEST_CONFIG, "pom1.xml");
        assertTrue(pom.exists());
        assertTrue(pom1.exists());
        try {
            BufferedReader brentrada = new BufferedReader(new FileReader(pom));
            String lineaEntrada = avanzaHastaPrimerGroupId(brentrada);
            BufferedReader bsalida = new BufferedReader(new FileReader(pom1));
            String lineaEntrada2 = avanzaHastaPrimerGroupId(bsalida);
            while (null != lineaEntrada) {
                if (lineaEntrada2.indexOf("${variableExiste}") > -1) {
                    assertTrue(lineaEntrada.indexOf("3.2.1") > -1);
                    encontradaVariable = true;
                } else {
                    assertEquals(lineaEntrada, lineaEntrada2);
                }
                lineaEntrada = brentrada.readLine();
                lineaEntrada2 = bsalida.readLine();
            }
            assertNull(lineaEntrada);
            assertNull(lineaEntrada2);
            assertTrue(encontradaVariable);
            bsalida.close();
            brentrada.close();
        } catch (Exception e) {
            fail("no se puede leer fichero " + e);
        }
    }
