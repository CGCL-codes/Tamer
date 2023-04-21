    public void doSerialize(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        it.jplag.jplagClient.Option instance = (it.jplag.jplagClient.Option) obj;
        if (instance.getLanguage() == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns2_myns2_string__java_lang_String_String_Serializer.serialize(instance.getLanguage(), ns1_language_QNAME, null, writer, context);
        if (new java.lang.Integer(instance.getMinimumMatchLength()) == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns2_myns2__int__int_Int_Serializer.serialize(new java.lang.Integer(instance.getMinimumMatchLength()), ns1_minimumMatchLength_QNAME, null, writer, context);
        if (instance.getSuffixes() != null) {
            for (int i = 0; i < instance.getSuffixes().length; ++i) {
                ns2_myns2_string__java_lang_String_String_Serializer.serialize(instance.getSuffixes()[i], ns1_suffixes_QNAME, null, writer, context);
            }
        }
        if (new Boolean(instance.isReadSubdirs()) == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns2_myns2__boolean__boolean_Boolean_Serializer.serialize(new Boolean(instance.isReadSubdirs()), ns1_readSubdirs_QNAME, null, writer, context);
        ns2_myns2_string__java_lang_String_String_Serializer.serialize(instance.getPathToFiles(), ns1_pathToFiles_QNAME, null, writer, context);
        ns2_myns2_string__java_lang_String_String_Serializer.serialize(instance.getBasecodeDir(), ns1_basecodeDir_QNAME, null, writer, context);
        ns2_myns2_string__java_lang_String_String_Serializer.serialize(instance.getStoreMatches(), ns1_storeMatches_QNAME, null, writer, context);
        ns2_myns2_string__java_lang_String_String_Serializer.serialize(instance.getClustertype(), ns1_clustertype_QNAME, null, writer, context);
        if (instance.getCountryLang() == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns2_myns2_string__java_lang_String_String_Serializer.serialize(instance.getCountryLang(), ns1_countryLang_QNAME, null, writer, context);
        if (instance.getTitle() == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns2_myns2_string__java_lang_String_String_Serializer.serialize(instance.getTitle(), ns1_title_QNAME, null, writer, context);
        if (instance.getOriginalDir() == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns2_myns2_string__java_lang_String_String_Serializer.serialize(instance.getOriginalDir(), ns1_originalDir_QNAME, null, writer, context);
    }
