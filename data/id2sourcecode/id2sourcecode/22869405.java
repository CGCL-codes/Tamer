    private void addFixedStereotypes() {
        Object mSt = ele.get(TAG_STEREOTYPE + "DefaultNamespace" + "." + "CSharp_Property_rw");
        if (mSt == null) {
            Object strCP = Model.getExtensionMechanismsFactory().buildStereotype("CSharp Property", model);
            Object tv = Model.getExtensionMechanismsFactory().buildTaggedValue("accessors", "read-only");
            Model.getExtensionMechanismsHelper().addTaggedValue(strCP, tv);
            ele.put(TAG_STEREOTYPE + "DefaultNamespace" + "." + "CSharp_Property_ro", strCP);
            strCP = Model.getExtensionMechanismsFactory().buildStereotype("CSharp Property", model);
            tv = Model.getExtensionMechanismsFactory().buildTaggedValue("accessors", "write-only");
            Model.getExtensionMechanismsHelper().addTaggedValue(strCP, tv);
            ele.put(TAG_STEREOTYPE + "DefaultNamespace" + "." + "CSharp_Property_wo", strCP);
            strCP = Model.getExtensionMechanismsFactory().buildStereotype("CSharp Property", model);
            tv = Model.getExtensionMechanismsFactory().buildTaggedValue("accessors", "read-and-write");
            Model.getExtensionMechanismsHelper().addTaggedValue(strCP, tv);
            ele.put(TAG_STEREOTYPE + "DefaultNamespace" + "." + "CSharp_Property_rw", strCP);
        }
    }
