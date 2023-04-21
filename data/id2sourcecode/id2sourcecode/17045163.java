    private void checkBasicAttributes(DicomObject obj, byte[] digest) throws NoSuchAlgorithmException {
        DicomElement elem = obj.get(Tag.SOPInstanceUID);
        assertNotNull("Missing SOP Instance UID !", elem);
        assertTrue("SOP Instance UID is empty", !elem.isEmpty());
        assertNotNull("Missing Series Instance UID !", elem = obj.get(Tag.SeriesInstanceUID));
        assertTrue("Series Instance UID is empty", !elem.isEmpty());
        assertNotNull("Missing Study Instance UID !", elem = obj.get(Tag.StudyInstanceUID));
        assertTrue("Study Instance UID is empty", !elem.isEmpty());
        assertNotNull("Missing SOP Class UID !", elem = obj.get(Tag.SOPClassUID));
        assertTrue("SOP Class UID is empty", !elem.isEmpty());
        assertNotNull("Missing SpecificCharacterSet !", elem = obj.get(Tag.SpecificCharacterSet));
        assertTrue("SpecificCharacterSet is empty", !elem.isEmpty());
        assertNotNull("Missing ConceptNameCodeSequence !", elem = obj.get(Tag.ConceptNameCodeSequence));
        assertNotNull("Missing InstanceCreationDate !", elem = obj.get(Tag.InstanceCreationDate));
        assertTrue("InstanceCreationDate is empty", !elem.isEmpty());
        assertNotNull("Missing InstanceCreationTime !", elem = obj.get(Tag.InstanceCreationTime));
        assertTrue("InstanceCreationTime is empty", !elem.isEmpty());
        byte[] ba = obj.getBytes(Tag.EncapsulatedDocument);
        assertEquals("EncapsulatedDocument incorrect!", "1234567890", new String(ba));
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(ba);
        assertTrue("SHA1 Digest not equal!", Arrays.equals(md.digest(), digest));
    }
