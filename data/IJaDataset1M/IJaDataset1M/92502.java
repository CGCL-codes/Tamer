package org.rapla.storage.dbrm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Writer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import org.apache.avalon.framework.configuration.Configuration;
import org.rapla.components.util.Command;
import org.rapla.components.util.CommandQueue;
import org.rapla.components.util.SerializableDateTimeFormat;
import org.rapla.entities.EntityNotFoundException;
import org.rapla.entities.RaplaType;
import org.rapla.entities.User;
import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.entities.storage.EntityResolver;
import org.rapla.entities.storage.RefEntity;
import org.rapla.entities.storage.internal.SimpleEntity;
import org.rapla.entities.storage.internal.SimpleIdentifier;
import org.rapla.framework.Container;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;
import org.rapla.plugin.RaplaExtensionPoints;
import org.rapla.server.RemoteMethod;
import org.rapla.server.RemoteServer;
import org.rapla.server.RemoteServiceCaller;
import org.rapla.server.RemoteStorage;
import org.rapla.server.RestartServer;
import org.rapla.server.internal.RemoteStorageImpl;
import org.rapla.server.internal.SessionExpiredException;
import org.rapla.storage.IOContext;
import org.rapla.storage.LocalCache;
import org.rapla.storage.RaplaSecurityException;
import org.rapla.storage.UpdateEvent;
import org.rapla.storage.UpdateResult;
import org.rapla.storage.impl.AbstractCachableOperator;
import org.rapla.storage.impl.EntityStore;
import org.rapla.storage.xml.RaplaInput;
import org.rapla.storage.xml.RaplaMainReader;

/** This operator can be used to modify and access data over the
 * network.  It needs an server-process providing the StorageService
 * (usually this is the default rapla-server).
 * <p>Sample configuration:
  <pre>
   &lt;remote-storage id="web">
   &lt;/remote-storate>
  </pre>
 * The messaging-client value contains the id of a
 * messaging-client-component which handles the
 * communication with the server.
 * The RemoteOperator provides also the Service {@link RemoteServiceCaller}
 *   @see org.rapla.components.rpc.MessagingClient
*/
public class RemoteOperator extends AbstractCachableOperator implements RemoteServiceCaller, RestartServer {

    ServerStub serv = new ServerStub();

    String username;

    String password;

    protected CommandQueue notifyQueue;

    private boolean bSessionActive = false;

    Connector connector;

    private boolean bReservationsFetched;

    private Date firstCachedDate = null;

    private Date lastCachedDate = null;

    private boolean isRestarting;

    public RemoteOperator(RaplaContext context, Configuration config) throws RaplaException {
        super(context);
        Container raplaMainContainer = ((Container) context.lookup(Container.class.getName()));
        raplaMainContainer.addContainerProvidedComponent(RaplaExtensionPoints.SERVLET_PAGE_EXTENSION, RaplaStorePage.class.getName(), "store", null);
        raplaMainContainer.addContainerProvidedComponentInstance(RestartServer.class.getName(), this);
        ((Container) context.lookup(Container.class.getName())).addContainerProvidedComponentInstance(RemoteServiceCaller.ROLE, this);
        connector = new HTTPConnector(context, config);
    }

    public void connect() throws RaplaException {
        throw new RaplaException("RemoteOperator doesn't support anonymous connect");
    }

    public void connect(String username, char[] password) throws RaplaException {
        this.username = username;
        this.password = new String(password);
        if (isConnected()) return;
        getLogger().info("Connecting to server and starting login..");
        doConnect();
        try {
            String clientVersion = i18n.getString("rapla.version");
            serv.checkServerVersion(clientVersion);
            serv.login(this.username, this.password);
            bSessionActive = true;
            updateToday();
            getLogger().info("login successfull");
        } catch (RaplaException ex) {
            disconnect();
            throw ex;
        }
        loadData();
        notifyQueue = org.rapla.components.util.CommandQueue.createCommandQueue();
    }

    public void saveData() throws RaplaException {
        throw new RaplaException("RemoteOperator doesn't support storing complete cache, yet!");
    }

    /** implementation specific. Should be private */
    public void serverHangup() {
        getLogger().warn("Server hangup");
        if (!isRestarting) {
            getLogger().error(getI18n().format("error.connection_closed", getConnectionName()));
        }
        isRestarting = false;
        new Thread() {

            public void run() {
                fireStorageDisconnected();
            }
        }.start();
    }

    public String getConnectionName() {
        return connector.getInfo();
    }

    private void doConnect() throws RaplaException {
        boolean bFailed = true;
        try {
            connector.start();
            bFailed = false;
        } catch (Exception e) {
            throw new RaplaException(i18n.format("error.connect", getConnectionName()), e);
        } finally {
            if (bFailed) disconnect();
        }
    }

    public boolean isConnected() {
        return bSessionActive;
    }

    public boolean supportsActiveMonitoring() {
        return true;
    }

    public void refresh() throws RaplaException {
        serv.refresh();
    }

    public void restartServer() throws RaplaException {
        isRestarting = true;
        serv.restartServer();
        fireStorageDisconnected();
    }

    /** disconnect from the server */
    public void disconnect() throws RaplaException {
        getLogger().info("Disconnecting from server");
        try {
            bSessionActive = false;
            if (notifyQueue != null) {
                notifyQueue.dequeueAll();
            }
            firstCachedDate = null;
            lastCachedDate = null;
            bReservationsFetched = false;
            connector.stop();
            cache.clearAll();
        } catch (Exception e) {
            throw new RaplaException("Could not disconnect", e);
        }
        fireStorageDisconnected();
        serv.logout(this.username);
    }

    private void addToCache(EntityList list, boolean useCache) throws RaplaException {
        EntityResolver entityResolver = createEntityResolver(list, useCache ? cache : null);
        synchronized (cache) {
            resolveEntities(list.iterator(), entityResolver);
            for (Iterator it = list.iterator(); it.hasNext(); ) {
                SimpleEntity entity = (SimpleEntity) it.next();
                cache.put(entity);
            }
        }
    }

    private void loadData() throws RaplaException {
        checkConnected();
        cache.clearAll();
        getLogger().debug("Getting Data..");
        addToCache(serv.getResources(), false);
        getLogger().debug("Data flushed");
    }

    protected void checkConnected() throws RaplaException {
        if (!bSessionActive) {
            if (username == null) {
                throw new RaplaException("Need to login first!");
            } else {
                throw new RaplaException(i18n.format("error.connection_closed", getConnectionName()));
            }
        }
    }

    @Override
    protected void addChangedDynamicTypeDependant(UpdateEvent evt, DynamicType type, boolean toRemove) throws RaplaException {
    }

    @Override
    protected void checkNoDependencies(Collection<RefEntity> entities, Set<RefEntity> storeObjects) {
    }

    protected long getCurrentTime() throws RaplaException {
        if (bSessionActive) return getServerTime(); else return super.getCurrentTime();
    }

    public void dispatch(UpdateEvent evt) throws RaplaException {
        checkConnected();
        UpdateEvent closure = createClosure(evt);
        check(closure);
        if (getLogger().isDebugEnabled()) {
            Iterator it = closure.getStoreObjects().iterator();
            while (it.hasNext()) {
                RefEntity entity = (RefEntity) it.next();
                getLogger().debug("dispatching store for: " + entity);
            }
            it = closure.getRemoveObjects().iterator();
            while (it.hasNext()) {
                RefEntity entity = (RefEntity) it.next();
                getLogger().debug("dispatching remove for: " + entity);
            }
        }
        serv.dispatch(closure);
        UpdateResult result = update(closure, true);
        fireStorageUpdated(result);
    }

    public Object createIdentifier(RaplaType raplaType) throws RaplaException {
        return serv.createIdentifier(raplaType);
    }

    /** we must override this method because we can't store the passwords on the client*/
    public void authenticate(String username, String password) throws RaplaException {
        RemoteStorage remoteMethod = getRemoteMethod(RemoteStorage.class);
        remoteMethod.authenticate(username, password);
    }

    public long getServerTime() throws RaplaException {
        RemoteStorage remoteMethod = getRemoteMethod(RemoteStorage.class);
        long serverTime = remoteMethod.getServerTime();
        return serverTime;
    }

    public boolean canChangePassword() {
        try {
            RemoteStorage remoteMethod = getRemoteMethod(RemoteStorage.class);
            boolean result = remoteMethod.canChangePassword();
            return result;
        } catch (RaplaException ex) {
            return false;
        }
    }

    public void changePassword(User user, char[] oldPassword, char[] newPassword) throws RaplaException {
        try {
            RemoteStorage remoteMethod = getRemoteMethod(RemoteStorage.class);
            remoteMethod.changePassword(user.getUsername(), new String(oldPassword), new String(newPassword));
        } catch (RaplaSecurityException ex) {
            throw new RaplaSecurityException(i18n.getString("error.wrong_password"));
        }
    }

    private void updateReservations(User user, Date start, Date end) throws RaplaException {
        if (!bReservationsFetched) {
            bReservationsFetched = true;
            firstCachedDate = start;
            lastCachedDate = end;
            addToCache(serv.getReservations(firstCachedDate, lastCachedDate), true);
            return;
        }
        if (firstCachedDate != null) {
            if (start == null || start.before(firstCachedDate)) {
                addToCache(serv.getReservations(start, firstCachedDate), true);
                firstCachedDate = start;
            }
        }
        if (lastCachedDate != null) {
            if (end == null || end.after(lastCachedDate)) {
                addToCache(serv.getReservations(lastCachedDate, end), true);
                lastCachedDate = end;
            }
        }
    }

    public RefEntity resolveId(Object id) throws EntityNotFoundException {
        try {
            return super.resolveId(id);
        } catch (EntityNotFoundException ex) {
            try {
                EntityList resolved = serv.getEntityRecursive(id);
                addToCache(resolved, true);
            } catch (RaplaException rex) {
                throw new EntityNotFoundException("Object for id " + id.toString() + " not found due to " + ex.getMessage());
            }
            return super.resolveId(id);
        }
    }

    public SortedSet getAppointments(User user, Date start, Date end) throws RaplaException {
        checkConnected();
        updateReservations(user, start, end);
        return super.getAppointments(user, start, end);
    }

    public List getReservations(User user, Date start, Date end) throws RaplaException {
        checkConnected();
        updateReservations(user, start, end);
        return super.getReservations(user, start, end);
    }

    private String readResultToString(InputStream input) throws IOException {
        InputStreamReader in = new InputStreamReader(input, "utf-8");
        char[] buf = new char[4096];
        StringBuffer buffer = new StringBuffer();
        while (true) {
            int len = in.read(buf);
            if (len == -1) {
                break;
            }
            buffer.append(buf, 0, len);
        }
        String result = buffer.toString();
        return result;
    }

    long clientRepositoryVerion = 0;

    public class ServerStub {

        Object call(Class service, Method method, Object[] args) throws RaplaException {
            try {
                if (args == null) {
                    args = new Object[] {};
                }
                InputStream stream = callInput(service, method, args);
                Class<?> returnType = method.getReturnType();
                if (returnType.isAssignableFrom(EntityList.class)) {
                    LocalCache cacheProx = cache;
                    if (method.getName().equals("getResources")) {
                        cacheProx = null;
                    }
                    EntityStore store = new EntityStore(cacheProx, cache.getSuperCategory());
                    return readIntoStore(stream, store);
                }
                String resultString = readResultToString(stream);
                return convertFromString(returnType, resultString);
            } catch (IOException ex) {
                throw new RaplaException(ex);
            } catch (ParseException ex) {
                throw new RaplaException(ex);
            }
        }

        InputStream callInput(String service, RemoteMethod method, String[] args) throws RaplaException {
            try {
                String methodName = method.method();
                Map argMap = createArgumentMap(method, args);
                if (service != null) {
                    methodName = service + "/" + methodName;
                }
                return connector.call(methodName, argMap);
            } catch (SessionExpiredException ex) {
                disconnect();
                throw ex;
            } catch (IOException ex) {
                throw new RaplaException(ex);
            }
        }

        InputStream callInput(Class service, Method method, Object[] args) throws RaplaException {
            try {
                String methodName = method.getName();
                Map argMap = createArgumentMap(method, args);
                if (service != null) {
                    methodName = service.getName() + "/" + methodName;
                }
                return connector.call(methodName, argMap);
            } catch (SessionExpiredException ex) {
                disconnect();
                throw ex;
            } catch (IOException ex) {
                throw new RaplaException(ex);
            }
        }

        private Map createArgumentMap(RemoteMethod method, String[] args) throws RaplaException {
            Map argMap = new HashMap();
            if (args.length != method.length()) {
                throw new RaplaException("Paramter list don't match Expected " + method.length() + " but was " + args.length);
            }
            for (int i = 0; i < args.length; i++) {
                String argName = method.arg(i);
                argMap.put(argName, args[i]);
            }
            return argMap;
        }

        private Map createArgumentMap(Method method, Object[] args) throws RaplaException {
            LinkedHashMap argMap = new LinkedHashMap();
            Class<?>[] parameterTypes = method.getParameterTypes();
            int length = parameterTypes.length;
            if (args.length != length) {
                throw new RaplaException("Paramter list don't match Expected " + length + " but was " + args.length);
            }
            for (int i = 0; i < args.length; i++) {
                method.toString();
                String argName = "" + i;
                Object value = args[i];
                String stringValue = convertToString(value);
                argMap.put(argName, stringValue);
            }
            return argMap;
        }

        public void login(String username, String password) throws RaplaException {
            RemoteServer remoteMethod = getRemoteMethod(RemoteServer.class);
            remoteMethod.login(username, password);
        }

        public void logout(String username) throws RaplaException {
            RemoteServer remoteMethod = getRemoteMethod(RemoteServer.class);
            remoteMethod.logout(username);
        }

        public void checkServerVersion(String clientVersion) throws RaplaException {
            RemoteServer remoteMethod = getRemoteMethod(RemoteServer.class);
            remoteMethod.checkServerVersion(clientVersion);
        }

        public EntityList getEntityRecursive(Object id) throws RaplaException {
            SimpleIdentifier castedId = (SimpleIdentifier) id;
            String idS = castedId.getTypeName() + "_" + String.valueOf(castedId.getKey());
            RemoteStorage remoteMethod = getRemoteMethod(RemoteStorage.class);
            return remoteMethod.getEntityRecursive(idS);
        }

        public EntityList getResources() throws RaplaException {
            RemoteStorage remoteMethod = getRemoteMethod(RemoteStorage.class);
            return remoteMethod.getResources();
        }

        public EntityList getReservations(Date start, Date end) throws RaplaException {
            RemoteStorage remoteMethod = getRemoteMethod(RemoteStorage.class);
            return remoteMethod.getReservations(start, end);
        }

        private EntityList readIntoStore(InputStream stream, EntityStore store) throws RaplaException {
            RaplaContext inputContext = new IOContext().createInputContext(serviceManager, store, idTable);
            RaplaInput xmlAdapter = new RaplaInput(getLogger().getChildLogger("reading"));
            RaplaMainReader contentHandler = new RaplaMainReader(inputContext);
            try {
                InputStreamReader in = new InputStreamReader(stream, "UTF-8");
                PipedOutputStream out = new PipedOutputStream();
                final BufferedReader bufin = new BufferedReader(in);
                final Writer bufout = new OutputStreamWriter(out, "UTF-8");
                new Thread() {

                    public void run() {
                        try {
                            int i = 0;
                            while (true) {
                                String line = bufin.readLine();
                                if (line == null) break;
                                bufout.write(line);
                                i++;
                            }
                            bufout.flush();
                            bufout.close();
                        } catch (IOException ex) {
                        }
                    }

                    ;
                }.start();
                {
                    PipedInputStream inPipe = new PipedInputStream(out);
                    xmlAdapter.read(new InputStreamReader(inPipe, "UTF-8"), contentHandler, false);
                }
            } catch (IOException e) {
                throw new RaplaException("Error retrieving Data ", e);
            }
            return new EntityList(store.getList());
        }

        public Object createIdentifier(RaplaType raplaType) throws RaplaException {
            RemoteStorage remoteMethod = getRemoteMethod(RemoteStorage.class);
            String id = remoteMethod.createIdentifier(raplaType);
            try {
                return LocalCache.getId(null, id);
            } catch (ParseException e) {
                throw new RaplaException("Invalid id Object", e);
            }
        }

        public void dispatch(UpdateEvent evt) throws RaplaException {
            try {
                String xml = RemoteStorageImpl.createUpdateEvent(serviceManager, cache, evt);
                RemoteStorage remoteMethod = getRemoteMethod(RemoteStorage.class);
                remoteMethod.dispatch(xml);
            } catch (IOException e) {
                throw new RaplaException("Error retrieving Data ", e);
            }
        }

        public void restartServer() throws RaplaException {
            RemoteStorage remoteMethod = getRemoteMethod(RemoteStorage.class);
            remoteMethod.restartServer();
        }

        public void refresh() throws RaplaException {
            RemoteStorage remoteMethod = getRemoteMethod(RemoteStorage.class);
            String clientRepoVersion = String.valueOf(clientRepositoryVerion);
            String xml = remoteMethod.refresh(clientRepoVersion);
            if (xml.length() < 50 && xml.indexOf("<uptodate/>") >= 0) {
            } else {
                RemoteOperator.this.refresh(xml);
            }
        }

        public void notifyUpdate() {
            if (isRestarting) return;
            notifyQueue.enqueue(new UpdateCommand());
        }
    }

    private void refresh(String xml) throws RaplaException {
        synchronized (getLock()) {
            UpdateEvent evt = RemoteStorageImpl.createUpdateEvent(serviceManager, xml, cache);
            Iterator it = evt.getStoreObjects().iterator();
            while (it.hasNext()) {
                SimpleEntity entity = (SimpleEntity) it.next();
                RefEntity cachedVersion = (RefEntity) cache.get(entity.getId());
                if (cachedVersion != null && cachedVersion.getVersion() >= entity.getVersion()) {
                    it.remove();
                    continue;
                }
                if (getLogger().isDebugEnabled()) getLogger().debug(" storing " + entity.getId() + " version: " + entity.getVersion());
            }
            RemoteOperator.super.resolveEntities(evt.getStoreObjects().iterator(), createEntityResolver(evt.getStoreObjects(), cache));
            it = evt.getRemoveObjects().iterator();
            while (it.hasNext()) {
                SimpleEntity entity = (SimpleEntity) it.next();
                RefEntity cachedVersion = (RefEntity) cache.get(entity.getId());
                if (cachedVersion == null) {
                    it.remove();
                    continue;
                }
                if (getLogger().isDebugEnabled()) getLogger().debug(" removing " + entity.getId() + " version: " + entity.getVersion());
            }
            RemoteOperator.super.resolveEntities(evt.getRemoveObjects().iterator(), createEntityResolver(evt.getStoreObjects(), cache));
            if (bSessionActive && (evt.getRemoveObjects().size() > 0 || evt.getStoreObjects().size() > 0)) {
                getLogger().info("Objects updated!");
                UpdateResult result = update(evt, false);
                clientRepositoryVerion = evt.getRepositoryVersion();
                fireStorageUpdated(result);
            }
            clientRepositoryVerion = evt.getRepositoryVersion();
        }
    }

    public void serverDisconnected() {
        bSessionActive = false;
    }

    class UpdateCommand implements Command {

        public void execute() {
            if (!bSessionActive) return;
            try {
                serv.refresh();
            } catch (Exception ex) {
                getLogger().error(ex.getMessage(), ex);
            }
        }
    }

    @Override
    public <T> T getRemoteMethod(final Class<T> a) {
        InvocationHandler proxy = new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object result = serv.call(a, method, args);
                return result;
            }
        };
        ClassLoader classLoader = getClass().getClassLoader();
        Class<T>[] interfaces = new Class[] { a };
        Object proxyInstance = Proxy.newProxyInstance(classLoader, interfaces, proxy);
        return (T) proxyInstance;
    }

    public static Object convertFromString(Class<?> returnType, String resultString) throws ParseException, RaplaException {
        if (resultString == null) {
            return null;
        }
        if (returnType.isAssignableFrom(Long.class) || returnType.isAssignableFrom(long.class)) {
            return Long.parseLong(resultString);
        }
        if (returnType.isAssignableFrom(Integer.class) || returnType.isAssignableFrom(int.class)) {
            return Integer.parseInt(resultString);
        }
        if (returnType.isAssignableFrom(Double.class) || returnType.isAssignableFrom(double.class)) {
            return Double.parseDouble(resultString);
        }
        if (returnType.isAssignableFrom(Date.class)) {
            if (resultString.trim().length() == 0) {
                return null;
            }
            SerializableDateTimeFormat format = new SerializableDateTimeFormat();
            boolean fillDate = false;
            Date result = format.parseDate(resultString, fillDate);
            return result;
        }
        if (returnType.isAssignableFrom(RaplaType.class)) {
            return RaplaType.find(resultString);
        }
        if (returnType.isAssignableFrom(Boolean.class) || returnType.isAssignableFrom(boolean.class)) {
            return Boolean.parseBoolean(resultString);
        }
        return resultString;
    }

    public static String convertToString(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            SerializableDateTimeFormat format = new SerializableDateTimeFormat();
            return format.formatDate((Date) value);
        }
        return value.toString();
    }
}
