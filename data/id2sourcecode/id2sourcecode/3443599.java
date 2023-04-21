    public void validate(ModelObject obj, ValidationContext context, ModelListener l) {
        Interaction interaction = (Interaction) obj;
        if (interaction.getMessageSignature() != null) {
            context.validate(interaction.getMessageSignature(), l);
        }
        if (interaction.getFromRole() == null && (interaction.getChannel() == null || interaction.getChannel().getFromRole() == null)) {
            if (context.getLocatedRole() == null || interaction.getToRole() == null || context.getLocatedRole().equals(interaction.getToRole().getName())) {
                l.error(new ModelIssue(obj, org.scribble.util.MessageUtil.format(java.util.PropertyResourceBundle.getBundle("org.scribble.validation.Messages"), "_INTERACTION_ROLE", new String[] { "from" })));
            }
        }
        if (interaction.getToRole() == null && (interaction.getChannel() == null || interaction.getChannel().getToRole() == null)) {
            if (context.getLocatedRole() == null || interaction.getFromRole() == null || context.getLocatedRole().equals(interaction.getFromRole().getName())) {
                l.error(new ModelIssue(obj, org.scribble.util.MessageUtil.format(java.util.PropertyResourceBundle.getBundle("org.scribble.validation.Messages"), "_INTERACTION_ROLE", new String[] { "to" })));
            }
        }
    }
