        @Override
        public int initialize(Node xmlConfig) throws ClassNotFoundException, KETLThreadException {
            super.initialize(xmlConfig);
            String type = this.getChannel();
            if (type.equalsIgnoreCase("SESSION")) {
                if (this.mObjectType != null) {
                    this.miSource = Arrays.searchArray(Sessionizer.ValidSessionColumnNames, this.mObjectType);
                    this.getXMLConfig().setAttribute("DATATYPE", Sessionizer.ValidSessionColumnTypes[this.miSource].getCanonicalName());
                }
                this.miType = Sessionizer.SESSION;
                if (this.miSource == Sessionizer.HITS) Sessionizer.this.mbHitsNeeded = true;
            } else if (type.equalsIgnoreCase("HIT")) {
                if (this.mObjectType != null) {
                    this.miSource = Arrays.searchArray(Sessionizer.ValidHitColumnNames, this.mObjectType);
                    this.getXMLConfig().setAttribute("DATATYPE", Sessionizer.ValidHitColumnTypes[this.miSource].getCanonicalName());
                }
                this.miType = Sessionizer.HIT;
                if (this.miSource == Sessionizer.ASSOCIATED_HITS) Sessionizer.this.mbHitsNeeded = true;
            } else throw new KETLThreadException("Channel must be HIT or SESSION", this);
            return 0;
        }
