    public void test_write_read() throws Exception {
        GB_StringAction l_sa;
        GB_SAReplaceBO l_bo = new GB_SAReplaceBO();
        l_bo.setFind("find");
        l_bo.setReplace("replace");
        l_bo.setIgnoreCase(true);
        l_bo.setRespectCase(false);
        l_sa = new GB_SAReplace(l_bo);
        test_write_read(1, l_sa);
        l_bo = (GB_SAReplaceBO) GB_BORandomTools.newBusinessObjectRandom(GB_SAReplaceBO.BO_NAME, false);
        l_sa = new GB_SAReplace(l_bo);
        test_write_read(2, l_sa);
        GB_StringAction[] l_sas = GB_SATestTools.getStringActionsTest();
        int len = CTools.getSize(l_sas);
        for (int i = 0; i < len; i++) {
            l_sa = l_sas[i];
            test_write_read(10 + i, l_sa);
        }
    }
