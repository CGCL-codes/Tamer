    public void testLdifDecodeEntry2() throws Exception {
        String ldifData = "" + "DN: cn=Test Area,cn=blog,cn=Chris,cn=topics,o=groupmind,c=au\n" + "objectClass: top\n" + "objectClass: groupmindEntry\n" + "groupmindCounter: 2\n" + "groupmindType: topic\n" + "groupmindAccess: write:cn=Chris,ou=users,o=groupmind,c=au\n" + "groupmindAccess: write:cn=Chris,cn=Chris,ou=users,o=groupmind,c=au\n" + "groupmindAccess: read:cn=public\n" + "groupmindAccess: write:cn=public\n" + "groupmindAccess: read:cn=Chris,cn=Chris,ou=users,o=groupmind,c=au\n" + "groupmindAccess: read:cn=Chris,ou=users,o=groupmind,c=au\n" + "groupmindRating: 10\n" + "groupmindAuthor: cn=Chris,ou=users,o=groupmind,c=au\n" + "uid: 3as\n" + "title: Test Area\n" + "cn: Test Area\n" + "description: This blog is useful for testing formatting.\n" + " > >\n" + " >\n" + " > >\n" + " >\n" + " > >\n" + " >alpha\n" + " > >\n" + " >\n" + " > >\n" + " >*beta\n" + " > >\n" + " >*gamma\n" + " > >\n" + " >delta\n" + " > >\n" + " >\n" + "groupmindTimestamp: 1215037384717";
        StringReader stringReader = new StringReader(ldifData);
        BufferedReader reader = new BufferedReader(stringReader);
        DXEntry entry1 = utility.readLdifEntry(reader);
        String result = "This blog is useful for testing formatting.\n" + " >\n" + "\n" + " >\n" + "\n" + " >\n" + "alpha\n" + " >\n" + "\n" + " >\n" + "*beta\n" + " >\n" + "*gamma\n" + " >\n" + "delta\n" + " >\n";
        assertEquals("cn=Test Area,cn=blog,cn=Chris,cn=topics,o=groupmind,c=au", entry1.getDN().toString());
        assertEquals(result, entry1.getString("description"));
    }
