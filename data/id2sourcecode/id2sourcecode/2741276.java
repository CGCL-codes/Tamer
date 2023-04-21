        public long digest() {
            if (this.digest == 0) {
                this.digest = _hashcode(digestGen.digest(this.data));
            }
            return this.digest;
        }
