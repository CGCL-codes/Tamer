    public void testValidInteraction() {
        String type = "Order";
        String role1Name = "Customer";
        Role role1 = new Role(role1Name);
        String role2Name = "Supplier";
        Role role2 = new Role(role2Name);
        String channelName = "Ch1";
        Channel ch1 = new Channel(channelName);
        String text = type + " from " + role1Name + " to " + role2Name + " via " + channelName + ";";
        ModelReference ref = new ModelReference("");
        TestParserContext context = new TestParserContext(ref, text);
        ErrorRecorder l = new ErrorRecorder();
        context.setState(role1Name, role1);
        context.setState(role2Name, role2);
        context.setState(channelName, ch1);
        Object obj = context.parse(Interaction.class, l);
        if (obj instanceof Interaction) {
            Interaction interaction = (Interaction) obj;
            if (interaction.getMessageSignature() == null) {
                fail("No message signature");
            } else if (interaction.getMessageSignature().getOperation() != null) {
                fail("No operation expected");
            } else if (interaction.getMessageSignature().getTypes().size() != 1) {
                fail("Only one message sig type expected: " + interaction.getMessageSignature().getTypes().size());
            } else if (interaction.getMessageSignature().getTypes().get(0).getAlias().equals(type) == false) {
                fail("Message sig type not expected '" + type + "': " + interaction.getMessageSignature().getTypes().get(0).getAlias());
            }
            if (interaction.getFromRole() == null) {
                fail("From role not set");
            } else if (interaction.getFromRole().equals(role1) == false) {
                fail("From role not expected: " + interaction.getFromRole());
            }
            if (interaction.getToRole() == null) {
                fail("To role not set");
            } else if (interaction.getToRole().equals(role2) == false) {
                fail("To role not expected: " + interaction.getToRole());
            }
            if (interaction.getChannel() == null) {
                fail("Channel not set");
            } else if (interaction.getChannel().equals(ch1) == false) {
                fail("Channel not expected");
            }
            if (l.getErrors().size() > 0) {
                fail("Not expecting " + l.getErrors().size() + " errors");
            } else if (l.getWarnings().size() > 0) {
                fail("Not expecting " + l.getWarnings().size() + " warnings");
            }
        } else if (obj == null) {
            fail("No object returned");
        } else {
            fail("Unexpected object type: " + obj.getClass());
        }
    }
