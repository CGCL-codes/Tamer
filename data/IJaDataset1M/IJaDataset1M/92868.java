package shellkk.qiq.jdm.engine.persist;

import java.util.ArrayList;
import java.util.List;
import javax.datamining.NamedObject;
import org.hibernate.Session;
import shellkk.qiq.jdm.JDMExceptionUtil;
import shellkk.qiq.jdm.MiningObjectImpl;
import shellkk.qiq.jdm.NamedObjectEx;
import shellkk.qiq.jdm.transformations.Transformation;
import shellkk.qiq.jdm.transformations.TransformationSequence;
import shellkk.qiq.jdm.transformations.TransformationSequenceItem;

public class TransformationSequencePersistEngine extends HibernatePersistEngine implements ITransformationSequencePersister {

    protected List<ITransformationAccessObject> daos = new ArrayList();

    public ITransformationAccessObject findDAO(TransformationSequenceItem item) {
        for (ITransformationAccessObject dao : daos) {
            if (dao.getTransformationType().name().equals(item.getTransTypeName())) {
                return dao;
            }
        }
        return null;
    }

    public List<ITransformationAccessObject> getDaos() {
        return daos;
    }

    public void setDaos(List<ITransformationAccessObject> daos) {
        this.daos = daos;
    }

    @Override
    protected Class getPersistClass() {
        return TransformationSequence.class;
    }

    @Override
    protected void delete(MiningObjectImpl obj, Session session) throws Exception {
        TransformationSequence transSeq = (TransformationSequence) obj;
        for (TransformationSequenceItem item : transSeq.getSeqItems()) {
            ITransformationAccessObject dao = findDAO(item);
            if (dao == null) {
                JDMExceptionUtil.throwUnsupportPersist(item.getTransformation().getClass());
            }
            dao.deleteTransformation(item, session);
        }
        session.delete(transSeq);
    }

    @Override
    protected void load(MiningObjectImpl obj, Session session) throws Exception {
        TransformationSequence transSeq = (TransformationSequence) obj;
        for (TransformationSequenceItem item : transSeq.getSeqItems()) {
            ITransformationAccessObject dao = findDAO(item);
            if (dao == null) {
                JDMExceptionUtil.throwUnsupportPersist(item.getTransformation().getClass());
            }
            Transformation trans = dao.loadTransformation(item, session);
            item.setTransformation(trans);
        }
    }

    @Override
    protected void saveOrUpdate(MiningObjectImpl obj, Session session) throws Exception {
        TransformationSequence transSeq = (TransformationSequence) obj;
        session.saveOrUpdate(transSeq);
        for (TransformationSequenceItem item : transSeq.getSeqItems()) {
            ITransformationAccessObject dao = findDAO(item);
            if (dao == null) {
                JDMExceptionUtil.throwUnsupportPersist(item.getTransformation().getClass());
            }
            dao.saveOrUpdateTransformation(item, session);
        }
    }

    public void delete(TransformationSequence sequence, Session session) throws Exception {
        for (TransformationSequenceItem item : sequence.getSeqItems()) {
            ITransformationAccessObject dao = findDAO(item);
            if (dao == null) {
                JDMExceptionUtil.throwUnsupportPersist(item.getTransformation().getClass());
            }
            dao.deleteTransformation(item, session);
        }
        session.delete(sequence);
    }

    public void load(TransformationSequence sequence, Session session) throws Exception {
        for (TransformationSequenceItem item : sequence.getSeqItems()) {
            ITransformationAccessObject dao = findDAO(item);
            if (dao == null) {
                JDMExceptionUtil.throwUnsupportPersist(item.getTransformation().getClass());
            }
            Transformation trans = dao.loadTransformation(item, session);
            item.setTransformation(trans);
        }
    }

    public void saveOrUpdate(TransformationSequence sequence, Session session) throws Exception {
        session.saveOrUpdate(sequence);
        for (TransformationSequenceItem item : sequence.getSeqItems()) {
            ITransformationAccessObject dao = findDAO(item);
            if (dao == null) {
                JDMExceptionUtil.throwUnsupportPersist(item.getTransformation().getClass());
            }
            dao.saveOrUpdateTransformation(item, session);
        }
    }

    public NamedObject getPersistObjectType() {
        return NamedObjectEx.transformationSequence;
    }

    @Override
    protected String getMinorTypeProperty() {
        return null;
    }
}
