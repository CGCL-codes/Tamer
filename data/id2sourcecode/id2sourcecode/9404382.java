    private synchronized void computeDigest() {
        if (this.doDigest == false) return;
        this.msgDigest.reset();
        for (int i = 0; i < this.array.length; i++) this.msgDigest.update(this.array.get(i));
        this.digest = this.msgDigest.digest();
        this.length = this.digest.length;
        this.doDigest = false;
    }
