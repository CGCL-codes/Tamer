    public void testPomConVariables() {
        try {
            FileUtils.copyFile(new File(SRC_TEST_CONFIG, "pom_conVariables.xml"), new File(SRC_TEST_CONFIG, "pom.xml"));
        } catch (IOException e) {
            fail("No se puede copiar fichero " + e);
        }
        Properties propiedades = new Properties();
        propiedades.setProperty("variableExiste", "1.2.3");
        CambiaPom cambia = new CambiaPom(null, new File(SRC_TEST_CONFIG), true, new SystemStreamLog(), propiedades);
        LinkedList<Artifact> listado = cambia.getListado();
        assertNotNull(listado);
        assertEquals(3, listado.size());
        int contador = 0;
        for (Artifact artefacto : listado) {
            if ("pom_version".equals(artefacto.getArtifactId())) {
                assertEquals("1.1.0", artefacto.getVersion());
                contador++;
            }
            if ("maven-plugin-api".equals(artefacto.getArtifactId())) {
                assertEquals("1.2.3", artefacto.getVersion());
                contador++;
            }
            if ("junit".equals(artefacto.getArtifactId())) {
                assertEquals("${variableNoExiste}", artefacto.getVersion());
                contador++;
            }
        }
        assertEquals(3, contador);
    }
