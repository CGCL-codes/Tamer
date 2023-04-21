    @Test
    public void testWriteValid1File() {
        String main_msg = "Testing read/write xrm.." + "\n\t" + "Error: ";
        String err_msg;
        storageHandler.setFile(wf);
        try {
            storageHandler.writeFileData(outputTemp, true);
            StorageHandler result = new StorageHandler();
            result.setXSDFile(fileXSD);
            result.setFile(new WorkingFile());
            result.readFileData(outputTemp);
            WorkingFile rwf = result.getFile();
            if (validArtifacts.size() != rwf.getAllArtifacts().size()) {
                err_msg = "The file does not have the correct number of artifacts." + "\n\tActual no of artifacts: " + rwf.getAllArtifactKeys() + "\n\tExpected no of artifacts: " + validArtifacts.size();
                assertTrue(main_msg + err_msg, false);
            }
            Iterator<Artifact> iter = validArtifacts.values().iterator();
            while (iter.hasNext()) {
                Artifact expArt = iter.next();
                Artifact resultArt = rwf.getArtifactByKey(expArt.getKey());
                if (resultArt == null) {
                    err_msg = "Could not find expected artifact named " + expArt.getTitle();
                    assertTrue(main_msg + err_msg, false);
                } else if (!resultArt.equals(expArt)) {
                    err_msg = "The expected artifact,(" + expArt.getTitle() + ") did not match resulted artifact (" + resultArt.getTitle() + ")";
                    assertTrue(main_msg + err_msg, false);
                }
            }
            Set<String> groups = groupList.keySet();
            Iterator<String> iter2 = groups.iterator();
            while (iter.hasNext()) {
                String groupName = iter2.next();
                Group g = rwf.getGroup(groupName);
                int numChild = groupList.get(groupName);
                for (int i = 1; i <= numChild; i++) {
                    String childName = groupName + "." + i;
                    if (!g.containsGroup(childName)) {
                        err_msg = groupName + "did not contain child group, " + childName;
                        assertTrue(main_msg + err_msg, false);
                    }
                    if (!rwf.getGroup(childName).getParent().getName().equals(groupName)) {
                        err_msg = "Checking that child group, " + childName + ", has the right parent group, " + groupName;
                        assertTrue(main_msg + err_msg, false);
                    }
                    Artifact[] groupArt = validgroupedArtifacts.get(groupName);
                    int expLength = groupArt.length;
                    int resultLength = g.getArtifacts().size();
                    if (expLength != resultLength) {
                        err_msg = g.getName() + " does not contain the expected number of artifacts (" + expLength + "). It contains " + resultLength + " artifacts.";
                        assertTrue(main_msg + err_msg, false);
                    }
                    for (int j = 0; j <= groupArt.length; j++) {
                        if (!g.containsArtifact(groupArt[j])) {
                            err_msg = g.getName() + " does not contain " + groupArt[j].getTitle();
                            assertTrue(main_msg + err_msg, false);
                        }
                    }
                }
            }
        } catch (Exception e) {
            assertTrue(main_msg + e.getMessage(), false);
        }
    }
