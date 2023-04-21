        MIBObject(MappedAttribute ma, boolean rowEntry) throws NotEnoughInformationException {
            this.name = ma.getName().substring(0, 1).toLowerCase() + ma.getName().substring(1);
            this.rowEntry = rowEntry;
            this.syntax = (ma.getSnmpType() != null) ? ma.getSnmpType() : "OCTET STRING (SIZE(0..255))";
            this.maxAccess = ma.getMaxAccess();
            if (this.maxAccess == null) {
                if (ma.isReadWrite()) maxAccess = "read-write"; else maxAccess = "read-only";
            }
            this.status = (ma.getStatus() != null) ? ma.getStatus() : "current";
            this.description = (ma.getSnmpDesc() != null) ? ma.getSnmpDesc() : "";
            String[] temp = ma.getOid().split("\\.");
            this.objectId = temp[temp.length - 1];
            this.fullOid = ma.getOidPrefix() + "." + this.objectId;
            if (this.rowEntry) this.oidDef = ma.getOidDefName(); else setOidDef(ma.getOidPrefix(), ma.getOidDefName());
        }
