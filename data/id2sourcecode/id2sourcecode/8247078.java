    Channel getChannelByIdRecursive(int id) {
        if (this.id == id) return this;
        for (Channel c : subChannels) {
            if (c.id == id) return c;
            Channel subC = c.getChannelByIdRecursive(id);
            if (subC != null) return subC;
        }
        return null;
    }
