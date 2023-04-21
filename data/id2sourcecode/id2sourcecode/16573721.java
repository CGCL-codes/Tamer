    @Test
    public void testMenuOpenSmiles() throws CDKException, ClassNotFoundException, IOException, CloneNotSupportedException {
        if (System.getProperty("os.name").indexOf("Mac") == -1) {
            String filename = "data/smiles.smi";
            InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
            File file = new File(System.getProperty("java.io.tmpdir") + File.separator + "test.smi");
            if (file.exists()) file.delete();
            FileOutputStream fos = new FileOutputStream(file);
            while (ins.available() > 0) fos.write(ins.read());
            applet.menuItem("open").click();
            DialogFixture dialog = applet.dialog();
            JComboBox combobox = dialog.robot.finder().find(new ComboBoxTextComponentMatcher("org.openscience.jchempaint.io.JCPFileFilter", "org.openscience.jchempaint.io.JCPSaveFileFilter"));
            combobox.setSelectedItem(combobox.getItemAt(2));
            JTextComponentFixture text = dialog.textBox();
            text.setText(file.toString());
            JButtonFixture okbutton = new JButtonFixture(dialog.robot, dialog.robot.finder().find(new ButtonTextComponentMatcher("Open")));
            okbutton.click();
            DialogFixture coordsdialog = new DialogFixture(applet.robot, applet.robot.finder().find(new DialogTitleComponentMatcher("No 2D coordinates")));
            JButtonFixture okbuttoncoordsdialog = new JButtonFixture(coordsdialog.robot, coordsdialog.robot.finder().find(new ButtonTextComponentMatcher("Yes")));
            okbuttoncoordsdialog.click();
            ins = this.getClass().getClassLoader().getResourceAsStream(filename);
            SMILESReader reader = new SMILESReader(ins);
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
