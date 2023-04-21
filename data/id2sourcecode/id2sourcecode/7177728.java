    public boolean register_object(String parent_naming_context_ior, String name, String kind, String obj_ior, boolean should_overwrite) {
        try {
            org.omg.CORBA.Object o = this._orb.string_to_object(parent_naming_context_ior);
            org.omg.CosNaming.NamingContext nc = null;
            try {
                nc = org.omg.CosNaming.NamingContextHelper.narrow(o);
            } catch (Exception e) {
                MsgBox.show("Error", "You must click on a NamingContext (not a final object) in NameService Browser tree \nbefore trying to register an object, so that we know where to register the new name.");
                return (false);
            }
            org.omg.CosNaming.NameComponent comp[] = new org.omg.CosNaming.NameComponent[1];
            comp[0] = new org.omg.CosNaming.NameComponent();
            comp[0].id = name;
            comp[0].kind = kind;
            o = _orb.string_to_object(obj_ior);
            if (should_overwrite) {
                try {
                    nc.unbind(comp);
                } catch (Exception e) {
                }
            }
            nc.bind(comp, o);
        } catch (Exception e) {
            MsgBox.show("Error", "The name you chose is already in use, and you did not choose to overwrite this name.");
            return (false);
        }
        return (true);
    }
