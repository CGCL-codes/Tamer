    public void testSave() {
        Mixer mixer = new Mixer();
        for (int i = 0; i < 3; i++) {
            Channel channel = new Channel();
            channel.setName(Integer.toString(i + 1));
            mixer.getChannels().addChannel(channel);
        }
        for (int i = 0; i < 3; i++) {
            Channel channel = new Channel();
            channel.setName("SubChannel" + i);
            mixer.getSubChannels().addChannel(channel);
        }
        Mixer clone = (Mixer) ObjectUtilities.clone(mixer);
        boolean isEqual = mixer.equals(clone);
        if (!isEqual) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("Problem with Mixer\n");
            buffer.append("Original Object\n");
            buffer.append(ToStringBuilder.reflectionToString(mixer) + "\n");
            buffer.append("Cloned Object\n");
            buffer.append(ToStringBuilder.reflectionToString(clone) + "\n");
            System.out.println(buffer.toString());
        }
        assertTrue(isEqual);
        Element elem1 = mixer.saveAsXML();
        Element elem2 = (clone).saveAsXML();
        assertEquals(elem1.getTextString(), elem2.getTextString());
    }
