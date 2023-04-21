        public void writeExternal(java.io.DataOutput out, RevisionJournal journal) throws IOException {
            Entity.SERIALIZER.writeExternal(out, journal);
            out.writeInt(journal.reads.size());
            for (Identifier identifier : journal.reads) Identifier.SERIALIZER.writeExternal(out, identifier);
            out.writeInt(journal.inserts.size());
            for (Identifier identifier : journal.inserts) Identifier.SERIALIZER.writeExternal(out, identifier);
            out.writeInt(journal.deletes.size());
            for (Identifier identifier : journal.deletes) Identifier.SERIALIZER.writeExternal(out, identifier);
            out.writeInt(journal.updates.size());
            for (Identifier identifier : journal.updates) Identifier.SERIALIZER.writeExternal(out, identifier);
        }
