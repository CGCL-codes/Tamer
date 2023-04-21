package org.objectstyle.cayenne.query;

import org.apache.commons.lang.StringUtils;
import org.objectstyle.cayenne.CayenneRuntimeException;
import org.objectstyle.cayenne.ObjectId;
import org.objectstyle.cayenne.exp.Expression;
import org.objectstyle.cayenne.exp.ExpressionFactory;
import org.objectstyle.cayenne.map.EntityResolver;
import org.objectstyle.cayenne.map.ObjEntity;
import org.objectstyle.cayenne.map.ObjRelationship;

/**
 * A query that selects objects related to a given object. It is intended for fetching
 * objects related to a given object using a mapped relationship. Cayenne uses it for this
 * purpose internally. RelationshipQuery works with either an ObjectId or a GlobalID for a
 * root object.
 * 
 * @since 1.2
 * @author Andrus Adamchik
 */
public class RelationshipQuery extends IndirectQuery {

    protected ObjectId objectId;

    protected String relationshipName;

    protected boolean refreshing;

    private RelationshipQuery() {
    }

    /**
     * Creates a RelationshipQuery. Same as
     * <em>new RelationshipQuery(objectID, relationshipName, true)</em>.
     * 
     * @param objectID ObjectId of a root object of the relationship.
     * @param relationshipName The name of the relationship.
     */
    public RelationshipQuery(ObjectId objectID, String relationshipName) {
        this(objectID, relationshipName, true);
    }

    /**
     * Creates a RelationshipQuery.
     * 
     * @param objectID ObjectId of a root object of the relationship.
     * @param relationshipName The name of the relationship.
     * @param refreshing whether objects should be refreshed
     */
    public RelationshipQuery(ObjectId objectID, String relationshipName, boolean refreshing) {
        if (objectID == null) {
            throw new CayenneRuntimeException("Null objectID");
        }
        if (objectID.isTemporary()) {
            throw new CayenneRuntimeException("Temporary id can't be used in RelationshipQuery: " + objectID);
        }
        this.objectId = objectID;
        this.relationshipName = relationshipName;
        this.refreshing = refreshing;
    }

    public QueryMetadata getMetaData(final EntityResolver resolver) {
        return new DefaultQueryMetadata() {

            public boolean isRefreshingObjects() {
                return refreshing;
            }

            public ObjEntity getObjEntity() {
                return (ObjEntity) getRelationship(resolver).getTargetEntity();
            }
        };
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public boolean isRefreshing() {
        return refreshing;
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    protected Query createReplacementQuery(EntityResolver resolver) {
        if (objectId.isTemporary() && !objectId.isReplacementIdAttached()) {
            throw new CayenneRuntimeException("Can't build a query for relationship '" + relationshipName + "' for temporary id: " + objectId);
        }
        ObjRelationship relationship = getRelationship(resolver);
        Expression qualifier = ExpressionFactory.matchDbExp(relationship.getReverseDbRelationshipPath(), objectId);
        SelectQuery query = new SelectQuery((ObjEntity) relationship.getTargetEntity(), qualifier);
        query.setRefreshingObjects(refreshing);
        return query;
    }

    /**
     * Returns a non-null relationship object for this query.
     */
    public ObjRelationship getRelationship(EntityResolver resolver) {
        if (objectId == null) {
            throw new CayenneRuntimeException("Can't resolve query - objectID is null.");
        }
        ObjEntity entity = resolver.lookupObjEntity(objectId.getEntityName());
        ObjRelationship relationship = (ObjRelationship) entity.getRelationship(relationshipName);
        if (relationship == null) {
            throw new CayenneRuntimeException("No relationship named " + relationshipName + " found in entity " + entity.getName() + "; object id: " + objectId);
        }
        return relationship;
    }

    /**
     * Overrides toString() outputting a short string with query class and relationship
     * name.
     */
    public String toString() {
        return StringUtils.substringAfterLast(getClass().getName(), ".") + ":" + getRelationshipName();
    }
}
