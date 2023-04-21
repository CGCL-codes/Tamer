    @Test
    public void testMenuOpenCml() throws CDKException, ClassNotFoundException, IOException, CloneNotSupportedException {
        if (System.getProperty("os.name").indexOf("Mac") == -1) {
            String filename = "data/a-pinen.cml";
            InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
            File file = new File(System.getProperty("java.io.tmpdir") + File.separator + "test.cml");
            if (file.exists()) file.delete();
            FileOutputStream fos = new FileOutputStream(file);
            while (ins.available() > 0) fos.write(ins.read());
            applet.menuItem("open").click();
            DialogFixture dialog = applet.dialog();
            JComboBox combobox = dialog.robot.finder().find(new ComboBoxTextComponentMatcher("org.openscience.jchempaint.io.JCPFileFilter"));
            combobox.setSelectedItem(combobox.getItemAt(1));
            JTextComponentFixture text = dialog.textBox();
            text.setText(file.toString());
            JButtonFixture okbutton = new JButtonFixture(dialog.robot, dialog.robot.finder().find(new ButtonTextComponentMatcher("Open")));
            okbutton.click();
            ins = this.getClass().getClassLoader().getResourceAsStream(filename);
            CMLReader reader = new CMLReader(ins);
            ChemFile chemFile = (ChemFile) reader.read((ChemObject) new ChemFile());
            Assert.assertNotNull(chemFile);
            List<IAtomContainer> containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
            JPanelFixture jcppanel = applet.panel("appletframe");
            JChemPaintPanel panel = (JChemPaintPanel) jcppanel.target;
            Assert.assertEquals(containersList.size(), containersList.size());
            Assert.assertEquals((containersList.get(0)).getAtomCount(), panel.getChemModel().getMoleculeSet().getAtomContainer(0).getAtomCount());
            Assert.assertEquals((containersList.get(0)).getBondCount(), panel.getChemModel().getMoleculeSet().getAtomContainer(0).getBondCount());
            restoreModelWithBasicmol();
        }
    }
