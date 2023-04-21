    public XplDeclarator(String n_name, String n_internalname, String n_externalname, boolean n_donotrender, int n_storage, String n_doc, String n_helpURL, String n_ldsrc, boolean n_iny, String n_inydata, String n_inyby, String n_lddata, String n_address, boolean n_atomicwrite, boolean n_atomicread, XplType n_type, XplNode n_aliasref, XplInitializerList n_i) {
        set_name(n_name);
        set_internalname(n_internalname);
        set_externalname(n_externalname);
        set_donotrender(n_donotrender);
        set_storage(n_storage);
        set_doc(n_doc);
        set_helpURL(n_helpURL);
        set_ldsrc(n_ldsrc);
        set_iny(n_iny);
        set_inydata(n_inydata);
        set_inyby(n_inyby);
        set_lddata(n_lddata);
        set_address(n_address);
        set_atomicwrite(n_atomicwrite);
        set_atomicread(n_atomicread);
        p_type = null;
        p_aliasref = null;
        p_i = null;
        set_type(n_type);
        set_aliasref(n_aliasref);
        set_i(n_i);
    }
