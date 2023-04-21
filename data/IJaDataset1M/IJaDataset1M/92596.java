package com.ivis.xprocess.web.framework;

import java.io.File;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.ApplicationContext;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import com.ivis.xprocess.core.Parameter;
import com.ivis.xprocess.core.Pattern;
import com.ivis.xprocess.core.Portfolio;
import com.ivis.xprocess.core.ProjectForecast;
import com.ivis.xprocess.core.TaskForecast;
import com.ivis.xprocess.framework.DataSource;
import com.ivis.xprocess.framework.XchangeElement;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.framework.impl.XchangeElementImpl;
import com.ivis.xprocess.framework.vcs.auth.VCSPasswordAuth;
import com.ivis.xprocess.framework.vcs.exceptions.VCSException;
import com.ivis.xprocess.framework.vcs.impl.svn.SVNEventHandler;
import com.ivis.xprocess.framework.vcs.impl.svn.XprocessMergerFactory;
import com.ivis.xprocess.framework.xml.IFileIndex;
import com.ivis.xprocess.util.UuidUtils;
import com.ivis.xprocess.web.commands.Commands;
import com.ivis.xprocess.web.commands.CommitSessionCommand;
import com.ivis.xprocess.web.framework.LicenseManager.Perspective;
import com.ivis.xprocess.web.spring.SpringContext;

/**
 * 
 * To be used by session clients (e.g. Servlets, Web Services)
 * 
 */
public class SessionDataSource implements ISessionDatasource, Serializable {

    private static final long serialVersionUID = 5830578580949497031L;

    private static final Logger logger = Logger.getLogger(SessionDataSource.class.getName());

    private IMasterDatasource myMasterDS;

    private VCSPasswordAuth auth;

    private SVNClientManager clientManager;

    private transient Map<String, TransientElement> contents = null;

    private transient Set<TransientXchangeElement> dirty;

    private Perspective[] perspectives;

    public SessionDataSource(IMasterDatasource master, VCSPasswordAuth auth) {
        this.myMasterDS = master;
        this.auth = auth;
        clientManager = createClientManager(auth);
        contents = new HashMap<String, TransientElement>();
        dirty = new HashSet<TransientXchangeElement>();
    }

    /**
	 * @return the username for the session
	 */
    public String getCurrentUserID() {
        return auth.getUserName();
    }

    public void beginReadOnlyOperation() {
        myMasterDS.beginReadOnlyOperation();
    }

    public void endReadOnlyOperation() {
        myMasterDS.endReadOnlyOperation();
    }

    public void beginWriteOperation() {
        myMasterDS.beginWriteOperation();
    }

    public void endWriteOperation() {
        myMasterDS.endWriteOperation();
    }

    public Portfolio getRoot() throws Exception {
        return myMasterDS.getRoot();
    }

    public Xelement findElement(String uuid) {
        return myMasterDS.findElement(uuid);
    }

    public DataSource getDataSource() {
        return myMasterDS.getDataSource();
    }

    /**
	 * Take the local transient version of the XchangeElement, the base
	 * transient and merge them all together with the latest. The latest then
	 * becomes the newest version.
	 * 
	 * @param base
	 *            - the base transient XchangeElement, a snap shot of the
	 *            XchangeElement before any changes
	 * @param local
	 *            - local transient version of the XchangeElement, the version
	 *            that the user has modified
	 */
    public void mergeAndSave(XchangeElement base, XchangeElement local) {
        myMasterDS.mergeAndSave(base, local);
    }

    /**
	 * Save the XchangeElement and add it to VCS (if it already has been added
	 * nothing happens)
	 * 
	 * @param element
	 *            the changed XchangeElement
	 */
    public void saveAndAdd(XchangeElement element) {
        myMasterDS.saveAndAdd(element);
    }

    public void requestCommit(File[] files) throws Exception {
        if ((files != null) && (files.length > 0)) {
            SpringContext ctx = SpringContext.getInstance();
            ApplicationContext app_ctx = ctx.getApplicationContext();
            if (!app_ctx.containsBean(Commands.COMMIT_COMMAND)) {
                throw new Exception("Bean " + Commands.COMMIT_COMMAND + " not found");
            }
            Object bean = app_ctx.getBean(Commands.COMMIT_COMMAND);
            if (!(bean instanceof CommitSessionCommand)) {
                throw new Exception("Bean " + Commands.COMMIT_COMMAND + " is not of type " + CommitSessionCommand.class.getName());
            }
            CommitSessionCommand commit_command = (CommitSessionCommand) bean;
            commit_command.setFilesToCommit(files);
            commit_command.setSource(this);
            commit_command.request();
        }
    }

    /**
	 * Called via command queue, do not invoke directly. Must be called in a
	 * write lock.
	 * 
	 * @param files
	 *            - all the files to commit
	 */
    public void commit(File[] files) {
        if ((files != null) && (files.length > 0)) {
            try {
                long rev = myMasterDS.getVCSManager().commit(files, this);
                logger.info("Committed " + files.length + " files to revision " + rev);
            } catch (VCSException e) {
                logger.log(Level.WARNING, "Failed to commit", e);
            }
        } else {
            logger.fine("Nothing to commit");
        }
    }

    /**
	 * Not on the interface - used by TransientExchangeElement
	 * 
	 * @param uuid
	 * @return the transient version of the XchangeElement,or null if the uuid
	 *         is no longer known or is a ghost.
	 * @throws Exception
	 */
    public XchangeElement createTransientExchangeElement(String uuid) throws Exception {
        return myMasterDS.createTransientExchangeElement(uuid);
    }

    @SuppressWarnings("unchecked")
    private TransientElement createWebTransient(Xelement xelement) {
        TransientXchangeElement txe = null;
        XchangeElement xe;
        if (xelement instanceof XchangeElement) {
            xe = (XchangeElement) xelement;
        } else {
            xe = xelement.getContainedIn();
        }
        Class<? extends TransientElement> clazz = TransientSchema.getTypeFor(xe.getUuid());
        if (clazz == null) {
            logger.warning("No type found for uuid " + xe.getUuid());
            return null;
        }
        try {
            Constructor constructor = clazz.getConstructor(XchangeElement.class, ISessionDatasource.class);
            txe = (TransientXchangeElement) constructor.newInstance(xe, this);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to instantiate transient element", e);
        }
        if (txe == null) {
            return null;
        }
        addToContents(txe);
        if (xelement instanceof XchangeElement) {
            return txe;
        }
        return contents.get(xelement.getUuid());
    }

    private boolean isDirty(TransientElement transientElement) {
        TransientXchangeElement txe = null;
        if (transientElement instanceof TransientXchangeElement) {
            txe = (TransientXchangeElement) transientElement;
        } else {
            txe = (TransientXchangeElement) contents.get(transientElement.containerUuid);
        }
        return dirty.contains(txe);
    }

    /**
	 * Returns a transient directly from the map. No stale check is performed.
	 * If the element does not exist, no attempt is made to load it and null is
	 * returned.
	 * 
	 * @param uuid
	 * @return the transient version of the element, or null if not in the map
	 */
    public TransientElement getTransientElementFromMap(String uuid) {
        return contents.get(uuid);
    }

    public TransientElement getTransientElement(String uuid) {
        if (!transientTypeChecker(uuid)) {
            return null;
        }
        TransientElement transientElement = contents.get(uuid);
        if ((transientElement != null) && isDirty(transientElement)) {
            return transientElement;
        }
        if (!transientTypeChecker(uuid)) {
            return null;
        }
        try {
            myMasterDS.beginReadOnlyOperation();
            Xelement xelement = myMasterDS.findElement(uuid);
            if (xelement == null) {
                if (transientElement != null) {
                    if (transientElement instanceof TransientXchangeElement) {
                        removeFromContents((TransientXchangeElement) transientElement);
                    } else {
                        TransientXchangeElement transientContainer = (TransientXchangeElement) contents.get(transientElement.getContainerUuid());
                        if (transientContainer != null) {
                            removeFromContents(transientContainer);
                        }
                    }
                }
                return null;
            }
            if (transientElement == null) {
                transientElement = createWebTransient(xelement);
            } else {
                if (transientElement.isStale()) {
                    transientElement = createWebTransient(xelement);
                }
            }
        } finally {
            myMasterDS.endReadOnlyOperation();
        }
        return transientElement;
    }

    private void addToContents(TransientXchangeElement txe) {
        if (contents.get(txe.getUuid()) != null) {
            removeFromContents(txe);
        }
        contents.put(txe.getUuid(), txe);
        for (TransientElement te : txe.getContents()) {
            contents.put(te.getUuid(), te);
        }
    }

    private void removeFromContents(TransientXchangeElement txe) {
        contents.remove(txe.uuid);
        for (TransientElement te : txe.getContents()) {
            contents.remove(te.getUuid());
        }
    }

    public void saveAndCommitDirtyElements() {
        File[] files = new File[dirty.size()];
        IFileIndex idx = myMasterDS.getDataSource().getPersistenceHelper().getFileIndex();
        int pos = 0;
        try {
            String path = null;
            myMasterDS.beginWriteOperation();
            Set<TransientXchangeElement> toSave = new HashSet<TransientXchangeElement>(dirty);
            for (TransientXchangeElement txe : toSave) {
                if (txe != null) {
                    txe.save();
                    path = idx.getFullPath(txe.getId());
                    if (path != null) {
                        files[pos++] = new File(path);
                    } else {
                        logger.warning("Path for transient element " + txe.getId() + " is null after saving");
                    }
                }
            }
            requestCommit(files);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to save or commit dirty transients", e);
        } finally {
            myMasterDS.endWriteOperation();
        }
        dirty.clear();
    }

    public void setDirty(TransientXchangeElement txe) {
        dirty.add(txe);
    }

    /**
	 * For JUnit use only!
	 * 
	 * @return all the contained uuids
	 */
    public Collection<String> getContainedUuids() {
        return contents.keySet();
    }

    public SVNClientManager getClientManager() {
        return clientManager;
    }

    public void setClean(TransientXchangeElement txe) {
        dirty.remove(txe);
    }

    /**
	 * For use with the web server Obtains a ClientManger for a specific
	 * password auth
	 * 
	 * @param auth
	 * @return the new SVNClientManager
	 */
    private SVNClientManager createClientManager(VCSPasswordAuth auth) {
        ISVNAuthenticationManager myAuth = new BasicAuthenticationManager(auth.getUserName(), auth.getPassword());
        DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
        options.setMergerFactory(new XprocessMergerFactory(getDataSource()));
        SVNClientManager clientManager = SVNClientManager.newInstance(options, myAuth);
        clientManager.getUpdateClient().setEventHandler(SVNEventHandler.getInstance());
        clientManager.getCommitClient().setEventHandler(SVNEventHandler.getInstance());
        return clientManager;
    }

    /**
	 * A debug utility method to output all the dirty elements.
	 * 
	 * @param out
	 *            - the PrintStream to output the dirty element to
	 */
    public void dumpDirtyElements(PrintStream out) {
        for (TransientXchangeElement element : dirty) {
            out.println(element.getUuid());
        }
    }

    public Pattern instantiatePattern(Pattern pattern, XchangeElement parent, Parameter... parameters) throws Exception {
        IFileIndex idx = myMasterDS.getDataSource().getPersistenceHelper().getFileIndex();
        Pattern instancePattern = null;
        try {
            myMasterDS.beginWriteOperation();
            instancePattern = pattern.instantiate(parent, null, parameters);
            Set<XchangeElement> prototypes = instancePattern.getPrototypes();
            File[] filesToCommit = new File[prototypes.size()];
            int i = 0;
            String path;
            for (XchangeElement xe : prototypes) {
                ((XchangeElementImpl) xe).save();
                path = idx.getFullPath(xe.getUuid());
                filesToCommit[i] = new File(path);
                myMasterDS.getVCSManager().add(filesToCommit[i], this);
                i++;
            }
            commit(filesToCommit);
        } finally {
            myMasterDS.endWriteOperation();
        }
        return instancePattern;
    }

    private boolean transientTypeChecker(String uuid) {
        Class type = UuidUtils.getTypeFromUUID(uuid);
        if (type == null) {
            logger.severe("getElement called with incorrect uuid " + uuid);
            return false;
        } else if (ProjectForecast.class.isAssignableFrom(type)) {
            logger.severe("getElement called with project forecast uuid " + uuid);
            return false;
        } else if (TaskForecast.class.isAssignableFrom(type)) {
            logger.severe("getElement called with task forecast uuid " + uuid);
            return false;
        } else {
            return true;
        }
    }

    /**
	 * For JUnit only!
	 * 
	 * @return all the dirty elements
	 */
    public Set<TransientXchangeElement> getDirtyElements() {
        return dirty;
    }

    public void logout() {
        myMasterDS.logout(this);
    }

    public VCSPasswordAuth getVCSAuth() {
        return auth;
    }

    public Perspective[] getPerspectives() {
        return perspectives;
    }

    public void setPerspectives(Perspective[] perspectives) {
        this.perspectives = perspectives;
    }
}
