    @Test
    public void multipleElements() throws IOException {
        String readId = "readId";
        Range range = Range.create(CoordinateSystem.RESIDUE_BASED, 10, 24);
        ConsensusNavigationElement element = new ConsensusNavigationElement(readId, range);
        sut.writeNavigationElement(element);
        sut.writeNavigationElement(new ReadNavigationElement("another" + readId, range.shiftLeft(3)));
        sut.close();
        StringBuilder expectedOutput = new StringBuilder("TITLE: " + title + "\n");
        expectedOutput.append("BEGIN_REGION\n").append(String.format("TYPE: %s\n", Type.CONSENSUS)).append(String.format("CONTIG: %s\n", element.getTargetId())).append(String.format("UNPADDED_CONS_POS: %d %d\n", range.getBegin(CoordinateSystem.RESIDUE_BASED), range.getEnd(CoordinateSystem.RESIDUE_BASED))).append("COMMENT: \n").append("END_REGION\n").append("BEGIN_REGION\n").append(String.format("TYPE: %s\n", Type.READ)).append(String.format("READ: another%s\n", element.getTargetId())).append(String.format("UNPADDED_READ_POS: %d %d\n", range.getBegin(CoordinateSystem.RESIDUE_BASED) - 3, range.getEnd(CoordinateSystem.RESIDUE_BASED) - 3)).append("COMMENT: \n").append("END_REGION\n");
        assertEquals(expectedOutput.toString(), new String(out.toByteArray()));
    }
