    protected Role findRole(Interaction interaction, boolean from) {
        Role ret = null;
        if (from) {
            ret = interaction.getFromRole();
            if (ret == null && interaction.getChannel() != null) {
                ret = interaction.getChannel().getFromRole();
            }
            if (ret == null) {
                Role other = interaction.getToRole();
                if (other == null && interaction.getChannel() != null) {
                    other = interaction.getChannel().getToRole();
                }
                if (other != null) {
                    Definition defn = interaction.getEnclosingDefinition();
                    if (defn != null && defn.getLocatedName() != null) {
                        ret = defn.getLocatedName().getRole();
                        if (ret != null && ret.equals(other)) {
                            ret = null;
                        }
                    }
                }
            }
        } else {
            ret = interaction.getToRole();
            if (ret == null && interaction.getChannel() != null) {
                ret = interaction.getChannel().getToRole();
            }
            if (ret == null) {
                Role other = interaction.getFromRole();
                if (other == null && interaction.getChannel() != null) {
                    other = interaction.getChannel().getFromRole();
                }
                if (other != null) {
                    Definition defn = interaction.getEnclosingDefinition();
                    if (defn != null && defn.getLocatedName() != null) {
                        ret = defn.getLocatedName().getRole();
                        if (ret != null && ret.equals(other)) {
                            ret = null;
                        }
                    }
                }
            }
        }
        return (ret);
    }
