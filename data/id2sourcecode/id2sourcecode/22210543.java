        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            DatabaseOperationBO operation = artifactData.getOperations().get(rowIndex);
            StringBuilder ucOperations = new StringBuilder();
            for (UseCaseOperationBO ucOperation : operation.getOperations()) {
                ucOperations.append("UCO/");
                ucOperations.append(ucOperation.getInProjectId());
                ucOperations.append("<br>");
            }
            StringBuilder readEntities = new StringBuilder();
            for (EntityClassPropertyBO readEntity : operation.getReadEntities()) {
                readEntities.append("ENT/");
                readEntities.append(readEntity.getEntityClass().getInProjectId());
                readEntities.append(": ");
                readEntities.append(readEntity.getEntityClass().getEntityName());
                readEntities.append(" - ");
                readEntities.append(readEntity.getProperty());
                readEntities.append("<br>");
            }
            StringBuilder writeEntities = new StringBuilder();
            for (EntityClassPropertyBO writeEntity : operation.getWriteEntities()) {
                writeEntities.append("ENT/");
                writeEntities.append(writeEntity.getEntityClass().getInProjectId());
                writeEntities.append(": ");
                writeEntities.append(writeEntity.getEntityClass().getEntityName());
                writeEntities.append(" - ");
                writeEntities.append(writeEntity.getProperty());
                writeEntities.append("<br>");
            }
            return new String[] { "DBO/" + operation.getInProjectId(), operation.getName(), ucOperations.toString(), readEntities.toString(), writeEntities.toString() }[columnIndex];
        }
