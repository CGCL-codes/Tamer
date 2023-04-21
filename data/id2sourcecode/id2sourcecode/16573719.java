    @Test
    public void testMenuOpenMol() throws CDKException, ClassNotFoundException, IOException, CloneNotSupportedException {
        if (System.getProperty("os.name").indexOf("Mac") == -1) {
            String filename = "data/chebi/ChEBI_26120.mol";
            InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
            File file = new File(System.getProperty("java.io.tmpdir") + File.separator + "test.mol");
            if (file.exists()) file.delete();
            FileOutputStream fos = new FileOutputStream(file);
            while (ins.available() > 0) fos.write(ins.read());
            applet.menuItem("open").click();
            DialogFixture dialog = applet.dialog();
            JTextComponentFixture text = dialog.textBox();
            text.setText(file.toString());
            JButtonFixture okbutton = new JButtonFixture(dialog.robot, dialog.robot.finder().find(new ButtonTextComponentMatcher("Open")));
            okbutton.click();
            ins = this.getClass().getClassLoader().getResourceAsStream(filename);
            MDLV2000Reader reader = new MDLV2000Reader(ins, Mode.STRICT);
            ChemFile chemFile = (ChemFile) reader.read((ChemObject) new ChemFile());
            Assert.assertNotNull(chemFile);
            List<IAtomContainer> containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
            JPanelFixture jcppanel = applet.panel("appletframe");
            JChemPaintPanel panel = (JChemPaintPanel) jcppanel.target;
            Assert.assertEquals(1, containersList.size());
            Assert.assertEquals((containersList.get(0)).getAtomCount(), panel.getChemModel().getMoleculeSet().getAtomContainer(0).getAtomCount());
            Assert.assertEquals((containersList.get(0)).getBondCount(), panel.getChemModel().getMoleculeSet().getAtomContainer(0).getBondCount());
            restoreModelWithBasicmol();
        }
    }
