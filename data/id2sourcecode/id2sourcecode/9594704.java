    @Test
    public void testSubstituteNameByValueCMLElement() throws Exception {
        String cmlS = "" + "<cml " + CMLConstants.CML_XMLNS + " id='a_i_'>" + "  <molecule id='mol_i_' title='orig mol'>" + "    <arg name='i' substitute='.//@*'>" + "      <scalar dataType='xsd:integer'>42</scalar>" + "    </arg>" + "    <atomArray>" + "      <atom id='m_i__a1'/>" + "      <atom id='m_i__a2'/>" + "    </atomArray>" + "  </molecule>" + "</cml>" + "";
        CMLCml cml = (CMLCml) CMLXOMTestUtils.parseValidString(cmlS);
        Assert.assertEquals("untouched id", "a_i_", cml.getId());
        Assert.assertEquals("child count", 1, cml.getChildElements().size());
        CMLMolecule mol = (CMLMolecule) cml.getChildCMLElements(CMLMolecule.TAG).get(0);
        Assert.assertEquals("mol id", "mol_i_", mol.getId());
        List<CMLAtom> atoms = mol.getAtoms();
        Assert.assertEquals("atom count", 2, atoms.size());
        Assert.assertEquals("atom id", "m_i__a1", atoms.get(0).getId());
        Assert.assertEquals("atom id", "m_i__a2", atoms.get(1).getId());
        CMLArg.substituteNameByValue(mol);
        Assert.assertEquals("untouched id", "a_i_", cml.getId());
        Assert.assertEquals("mol id", "mol42", mol.getId());
        Assert.assertEquals("atom count", 2, atoms.size());
        Assert.assertEquals("atom id", "m42_a1", atoms.get(0).getId());
        Assert.assertEquals("atom id", "m42_a2", atoms.get(1).getId());
        URL url = Util.getResource(SIMPLE_RESOURCE + File.separator + "arg1.xml");
        CMLCml arg1Cml = null;
        InputStream in = null;
        try {
            in = url.openStream();
            arg1Cml = (CMLCml) new CMLBuilder().build(in).getRootElement();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("EXC" + e);
        } finally {
            in.close();
        }
        CMLArg.substituteNameByValue(arg1Cml);
        CMLMolecule arg1Mol = (CMLMolecule) arg1Cml.getChildCMLElements(CMLMolecule.TAG).get(0);
        Assert.assertEquals("untouched id", "a_i_", cml.getId());
        Assert.assertEquals("mol id", "oh42", arg1Mol.getId());
        atoms = arg1Mol.getAtoms();
        Assert.assertEquals("atom count", 2, atoms.size());
        Assert.assertEquals("atom id", "oh42_a1", atoms.get(0).getId());
        Assert.assertEquals("atom id", "oh42_r1", atoms.get(1).getId());
        List<CMLBond> bonds = arg1Mol.getBonds();
        Assert.assertEquals("bond count", 2, atoms.size());
        Assert.assertEquals("bond id", "b_oh42_a1_oh42_r1", bonds.get(0).getId());
        StringTestBase.assertEquals("bond atomRefs2", new String[] { "oh42_a1", "oh42_r1" }, bonds.get(0).getAtomRefs2());
    }
