package com.db4o.nb.editor.nodes;

import com.db4o.ObjectContainer;
import com.db4o.ext.ExtObjectContainer;
import com.db4o.ext.ObjectInfo;
import com.db4o.nb.util.ClassLoaderUtil;
import com.db4o.nb.util.DialogUtil;
import com.db4o.reflect.ReflectClass;
import com.db4o.reflect.ReflectField;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.ErrorManager;
import org.openide.actions.PropertiesAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;

/**
 * Manages the representation of a single object instance.
 * 
 * @author klevgert
 */
public class ObjectNode extends AbstractNode {

    private static final String ICON_PATH = "com/db4o/nb/editor/resources/bean.gif";

    private long oid;

    private ObjectInfo objectInfo;

    private ObjectContainer objectContainer;

    /** Creates a new instance of ObjectNode */
    public ObjectNode(long oid, ObjectContainer oc) {
        super(new ObjectNodeChildren(oid, oc));
        this.oid = oid;
        this.objectContainer = oc;
        Object obj = null;
        try {
            obj = this.objectContainer.ext().getByID(oid);
        } catch (com.db4o.ext.Db4oException e) {
            ErrorManager.getDefault().log(ErrorManager.EXCEPTION, e.getMessage());
        }
        String displayName = "[" + oid + "] " + ((obj != null) ? obj.toString() : "");
        this.setDisplayName(displayName);
        this.setIconBaseWithExtension(ICON_PATH);
    }

    public long getOid() {
        return oid;
    }

    public ObjectContainer getObjectContainer() {
        return objectContainer;
    }

    public Action[] getActions(boolean popup) {
        return new Action[] { new AddObjectAction(this), new DeleteObjectAction(this), null, SystemAction.get(PropertiesAction.class) };
    }

    /**
   * Gets the value of an object's field.
   * @param fieldName   name of the field.
   * @return value of the field.
   */
    public Object getFieldValue(String fieldName) {
        try {
            Node node = this.getChildren().findChild(fieldName);
            if (node != null && node instanceof FieldNode) {
                FieldNode fieldNode = (FieldNode) node;
                return fieldNode.getFieldValue();
            } else {
                ObjectContainer oc = this.getObjectContainer();
                Object object = this.objectInfo.getObject();
                ReflectClass rc = oc.ext().reflector().forObject(object);
                ReflectField rf = rc.getDeclaredField(fieldName);
                return rf.get(object);
            }
        } catch (com.db4o.ext.Db4oException e) {
            ErrorManager.getDefault().log(ErrorManager.EXCEPTION, e.getMessage());
        }
        return null;
    }

    /**
   * Sets the value for a field in the object instance.
   * @param fieldName   name of the field.
   * @param value       new value of the field.
   *
   */
    public void setFieldValue(String fieldName, Object value) {
        try {
            Node node = this.getChildren().findChild(fieldName);
            if (node != null && node instanceof FieldNode) {
                FieldNode fieldNode = (FieldNode) node;
                fieldNode.setFieldValue(value);
            } else {
                ObjectContainer oc = this.getObjectContainer();
                Object object = this.objectInfo.getObject();
                ReflectClass rc = oc.ext().reflector().forObject(object);
                ReflectField rf = rc.getDeclaredField(fieldName);
                rf.set(object, value);
                oc.set(object);
            }
        } catch (com.db4o.ext.Db4oException e) {
            ErrorManager.getDefault().log(ErrorManager.EXCEPTION, e.getMessage());
        } catch (Exception e) {
            ErrorManager.getDefault().log(ErrorManager.EXCEPTION, e.getMessage());
        }
    }

    /**
   * Creates the properties sheet.
   * @return the properties sheet.
   */
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        Sheet.Set set = sheet.createPropertiesSet();
        set.setName("Db4o Properties");
        set.setDisplayName(NbBundle.getMessage(this.getClass(), "Db4oProperties"));
        Sheet.Set set2 = sheet.createPropertiesSet();
        set2.setName("Custom Properties");
        set2.setDisplayName(NbBundle.getMessage(this.getClass(), "CustomProperties"));
        try {
            createOidProperty(set);
            createInfoProperty(set);
            createVersionProperty(set);
            createCachedProperty(set);
            createActiveProperty(set);
            createFieldProperties(set2);
        } catch (Exception ex) {
            ErrorManager.getDefault().log(ex.getMessage());
        }
        sheet.put(set);
        sheet.put(set2);
        return sheet;
    }

    private void createFieldProperties(final Sheet.Set set2) throws ClassNotFoundException {
        ReflectClass rc = this.getReferencedClass();
        ReflectField[] fields = rc.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            final ReflectField field = fields[i];
            createFieldProperty(set2, field);
        }
        ReflectClass parent = rc.getSuperclass();
        while (parent != null) {
            fields = parent.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                final ReflectField field = fields[i];
                createFieldProperty(set2, field);
            }
            parent = parent.getSuperclass();
        }
    }

    private void createFieldProperty(final Sheet.Set set, final ReflectField field) throws ClassNotFoundException {
        final Object obj = this.objectContainer.ext().getByID(this.oid);
        this.objectContainer.activate(obj, 2);
        Class clazz = null;
        Property fieldProp = null;
        try {
            clazz = this.findClass(field);
            fieldProp = new PropertySupport.ReadWrite(field.getName(), clazz, field.getName(), null) {

                public Object getValue() {
                    Object fieldValue = ObjectNode.this.getFieldValue(field.getName());
                    return (fieldValue != null) ? fieldValue : "null!";
                }

                public void setValue(Object value) {
                    ObjectNode.this.setFieldValue(this.getName(), value);
                }
            };
        } catch (ClassNotFoundException ex) {
            ErrorManager.getDefault().log(ex.getMessage());
            fieldProp = new PropertySupport.ReadOnly(field.getName(), String.class, field.getName(), null) {

                public Object getValue() {
                    return ObjectNode.this.getFieldValue(field.getName());
                }
            };
        }
        if (fieldProp != null) {
            fieldProp.setName(field.getName());
            set.put(fieldProp);
        }
    }

    private void createOidProperty(final Sheet.Set set) {
        String displayName = NbBundle.getMessage(this.getClass(), "OID");
        Property oidProp = new PropertySupport.ReadOnly("oid", String.class, displayName, null) {

            public Object getValue() {
                long oid = ObjectNode.this.getOid();
                return "" + oid;
            }
        };
        oidProp.setName("oid");
        set.put(oidProp);
    }

    private void createInfoProperty(final Sheet.Set set) {
        String displayName = NbBundle.getMessage(this.getClass(), "Info");
        Property infoProp = new PropertySupport.ReadOnly("info", String.class, displayName, null) {

            public Object getValue() {
                long oid = ObjectNode.this.getOid();
                return ObjectNode.this.getObjectContainer().ext().getByID(oid).toString();
            }
        };
        infoProp.setName("info");
        set.put(infoProp);
    }

    private void createVersionProperty(final Sheet.Set set) {
        String displayName = NbBundle.getMessage(this.getClass(), "Version");
        Property versionProp = new PropertySupport.ReadOnly("version", Long.class, displayName, null) {

            public Object getValue() {
                long oid = ObjectNode.this.getOid();
                Object obj = ObjectNode.this.getObjectContainer().ext().getByID(oid);
                return new Long(ObjectNode.this.getObjectContainer().ext().getObjectInfo(obj).getVersion());
            }
        };
        versionProp.setName("version");
        set.put(versionProp);
    }

    private void createCachedProperty(final Sheet.Set set) {
        String displayName = NbBundle.getMessage(this.getClass(), "Cached");
        Property cachedProp = new PropertySupport.ReadOnly("cached", Boolean.class, displayName, null) {

            public Object getValue() {
                long oid = ObjectNode.this.getOid();
                return new Boolean(ObjectNode.this.getObjectContainer().ext().isCached(oid));
            }
        };
        cachedProp.setName("cached");
        set.put(cachedProp);
    }

    private void createActiveProperty(final Sheet.Set set) {
        String displayName = NbBundle.getMessage(this.getClass(), "Active");
        Property cachedProp = new PropertySupport.ReadOnly("active", Boolean.class, displayName, null) {

            public Object getValue() {
                long oid = ObjectNode.this.getOid();
                Object obj = ObjectNode.this.getObjectContainer().ext().getByID(oid);
                return new Boolean(ObjectNode.this.getObjectContainer().ext().isActive(obj));
            }
        };
        cachedProp.setName("active");
        set.put(cachedProp);
    }

    private Class findClass(ReflectField field) throws ClassNotFoundException {
        ReflectClass rc = this.getReferencedClass();
        String name = rc.getName();
        Class clazz = null;
        if (!rc.isPrimitive()) {
            Object fieldValue = this.getFieldValue(field.getName());
            if (fieldValue == null) {
                clazz = ClassLoaderUtil.getClassLoader().loadClass(name);
            } else {
                clazz = fieldValue.getClass();
            }
        } else {
            if (name.equals("int")) {
                clazz = int.class;
            } else if (name.equals("long")) {
                clazz = long.class;
            } else if (name.equals("double")) {
                clazz = double.class;
            } else if (name.equals("float")) {
                clazz = float.class;
            }
        }
        return clazz;
    }

    private ReflectClass getReferencedClass() {
        ObjectContainer oc = this.getObjectContainer();
        Object object = this.objectContainer.ext().getByID(this.oid);
        ReflectClass rc = oc.ext().reflector().forObject(object);
        return rc;
    }

    /**
   * Action that adds a new object.
   */
    public class AddObjectAction extends AbstractAction {

        private ObjectNode node;

        public AddObjectAction(ObjectNode node) {
            String displayName = NbBundle.getMessage(this.getClass(), "NewObject");
            this.putValue(NAME, displayName);
            this.node = node;
        }

        public void actionPerformed(ActionEvent e) {
            ExtObjectContainer eoc = ObjectNode.this.getObjectContainer().ext();
            Object obj = eoc.getByID(ObjectNode.this.getOid());
            eoc.activate(obj, 2);
            ReflectClass rc = eoc.reflector().forObject(obj);
            Object newInstance = rc.newInstance();
            eoc.set(newInstance);
            long newOid = eoc.getID(newInstance);
            AbstractNode classNode = (AbstractNode) ObjectNode.this.getParentNode();
            classNode.getChildren().add(new Node[] { new ObjectNode(newOid, eoc) });
        }
    }

    /**
   * Action that deletes the object associated with that object node.
   */
    public class DeleteObjectAction extends AbstractAction {

        private ObjectNode node;

        public DeleteObjectAction(ObjectNode node) {
            String displayName = NbBundle.getMessage(this.getClass(), "DeleteObject");
            this.putValue(NAME, displayName);
            this.node = node;
        }

        public void actionPerformed(ActionEvent e) {
            String message = NbBundle.getMessage(this.getClass(), "confirmDeleteObject");
            if (DialogUtil.confirmYesNo(message).equals(DialogUtil.YES)) {
                Object object = ObjectNode.this.getObjectContainer().ext().getByID(ObjectNode.this.getOid());
                ObjectNode.this.getObjectContainer().delete(object);
                AbstractNode classNode = (AbstractNode) ObjectNode.this.getParentNode();
                classNode.getChildren().remove(new Node[] { ObjectNode.this });
            }
        }
    }

    /**
   * Holds the sub nodes of an object node.
   * @author klevgert
   */
    public static class ObjectNodeChildren extends Index.ArrayChildren {

        private static final String ICON_PATH = "com/db4o/nb/editor/resources/field.gif";

        private long oid;

        private ObjectContainer objectContainer;

        /** 
     * Creates a new instance of ObjectChildren 
     * @param oid     object identifier
     * @param oc      db4o object container
     */
        public ObjectNodeChildren(long oid, ObjectContainer oc) {
            this.oid = oid;
            this.objectContainer = oc;
        }

        /**
     * Initializes the collection of child nodes.
     * @return child nodes.
     */
        protected Collection initCollection() {
            ArrayList children = new ArrayList();
            try {
                ExtObjectContainer eoc = this.objectContainer.ext();
                Object obj = eoc.getByID(this.oid);
                eoc.activate(obj, 2);
                ReflectClass rc = eoc.reflector().forObject(obj);
                ReflectField[] flds = rc.getDeclaredFields();
                for (int i = 0; i < flds.length; i++) {
                    AbstractNode fnode = new FieldNode(flds[i], obj, this.objectContainer);
                    children.add(fnode);
                }
                ReflectClass parent = rc.getSuperclass();
                while (parent != null) {
                    flds = parent.getDeclaredFields();
                    this.objectContainer.activate(obj, 2);
                    for (int i = 0; i < flds.length; i++) {
                        AbstractNode fnode = new FieldNode(flds[i], obj, this.objectContainer);
                        children.add(fnode);
                    }
                    parent = parent.getSuperclass();
                }
            } catch (Exception e) {
                String errmsg = NbBundle.getMessage(this.getClass(), "CannotAccessContent", ((e != null) ? e.toString() : ""));
                DialogUtil.showErrorMessage(errmsg);
                ErrorManager.getDefault().log(ErrorManager.ERROR, e.toString());
            }
            return children;
        }
    }
}
