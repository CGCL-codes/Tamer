            public boolean appliesTo(IEnvelope obj) {
                return obj.getChannel().equals(channel) && obj.getMessage().getClass().equals(msgClazz);
            }
