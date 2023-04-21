    protected static void saveCoreSubmesh(DataOutput out, CalCoreSubmesh coreSubmesh) throws IOException {
        out.writeInt(coreSubmesh.getCoreMaterialThreadId());
        CalCoreSubmesh.VertexInfo[] vectorVertex = coreSubmesh.getVectorVertexInfo();
        Vector3fBuffer vertexPositions = coreSubmesh.getVertexPositions();
        Vector3fBuffer vertexNormals = coreSubmesh.getVertexNormals();
        CalCoreSubmesh.Face[] vectorFace = coreSubmesh.getVectorFace();
        float[] vectorPhysicalProperty = coreSubmesh.getVectorPhysicalProperty();
        CalCoreSubmesh.Spring[] vectorSpring = coreSubmesh.getVectorSpring();
        out.writeInt(vectorVertex.length);
        out.writeInt(vectorFace.length);
        out.writeInt(coreSubmesh.getLodCount());
        out.writeInt(coreSubmesh.getSpringCount());
        TexCoord2fBuffer[] textureCoordinates = coreSubmesh.getTextureCoordinates();
        out.writeInt(textureCoordinates.length);
        for (int vertexId = 0; vertexId < vectorVertex.length; vertexId++) {
            CalCoreSubmesh.VertexInfo vertexInfo = vectorVertex[vertexId];
            Vector3f vertexPosition = vertexPositions.get(vertexId);
            Vector3f vertexNormal = vertexNormals.get(vertexId);
            out.writeFloat(vertexPosition.x);
            out.writeFloat(vertexPosition.y);
            out.writeFloat(vertexPosition.z);
            out.writeFloat(vertexNormal.x);
            out.writeFloat(vertexNormal.y);
            out.writeFloat(vertexNormal.z);
            out.writeInt(vertexInfo.collapseId);
            out.writeInt(vertexInfo.faceCollapseCount);
            for (int textureCoordinateId = 0; textureCoordinateId < textureCoordinates.length; textureCoordinateId++) {
                Vector2f texCoord = textureCoordinates[textureCoordinateId].get(vertexId);
                out.writeFloat(texCoord.x);
                out.writeFloat(texCoord.y);
            }
            out.writeInt(vertexInfo.influenceBoneIds.length);
            for (int influenceId = 0; influenceId < vertexInfo.influenceBoneIds.length; influenceId++) {
                out.writeInt(vertexInfo.influenceBoneIds[influenceId]);
                out.writeFloat(vertexInfo.influenceWeights[influenceId]);
            }
            if (coreSubmesh.getSpringCount() > 0) {
                out.writeFloat(vectorPhysicalProperty[vertexId]);
            }
        }
        for (int springId = 0; springId < coreSubmesh.getSpringCount(); springId++) {
            CalCoreSubmesh.Spring spring = vectorSpring[springId];
            out.writeInt(spring.vertexId0);
            out.writeInt(spring.vertexId1);
            out.writeFloat(spring.springCoefficient);
            out.writeFloat(spring.idleLength);
        }
        for (int faceId = 0; faceId < vectorFace.length; faceId++) {
            CalCoreSubmesh.Face face = vectorFace[faceId];
            out.writeInt(face.vertexId[0]);
            out.writeInt(face.vertexId[1]);
            out.writeInt(face.vertexId[2]);
        }
    }
