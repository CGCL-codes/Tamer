package com.duroty.task;

import com.duroty.hibernate.Identity;
import com.duroty.hibernate.MailPreferences;
import com.duroty.hibernate.Roles;
import com.duroty.hibernate.UserRole;
import com.duroty.hibernate.UserRoleId;
import com.duroty.hibernate.Users;
import com.duroty.lucene.utils.FileUtilities;
import com.duroty.utils.GeneralOperations;
import com.duroty.utils.log.DLog;
import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.jboss.varia.scheduler.Schedulable;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class InitServerTask implements Schedulable {

    /**
     * DOCUMENT ME!
     */
    private Context ctx = null;

    /**
     * DOCUMENT ME!
     */
    private boolean init = false;

    /**
     * DOCUMENT ME!
     */
    private String hibernateSessionFactory;

    /**
     * Creates a new InitServerTask object.
     *
     * @param poolSize DOCUMENT ME!
     * @throws NamingException
     *
     * @throws ClassNotFoundException DOCUMENT ME!
     * @throws NamingException DOCUMENT ME!
     * @throws IOException 
     * @throws InstantiationException DOCUMENT ME!
     * @throws IllegalAccessException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public InitServerTask(String hibernateSessionFactory) throws NamingException, IOException {
        super();
        ctx = new InitialContext();
        this.hibernateSessionFactory = hibernateSessionFactory;
        String tempDir = System.getProperty("java.io.tmpdir");
        if (!tempDir.endsWith(File.separator)) {
            tempDir = tempDir + File.separator;
        }
        FileUtilities.deleteMotLocks(new File(tempDir));
        FileUtilities.deleteLuceneLocks(new File(tempDir));
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     * @param arg1 DOCUMENT ME!
     */
    public void perform(Date arg0, long arg1) {
        if (isInit()) {
            DLog.log(DLog.DEBUG, this.getClass(), "InitServerTask is running and wait.");
            return;
        }
        try {
            setInit(true);
            flush();
        } catch (Exception e) {
            DLog.log(DLog.ERROR, this.getClass(), e);
        } finally {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param dirUsers DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void flush() throws Exception {
        SessionFactory hfactory = null;
        Session hsession = null;
        try {
            hfactory = (SessionFactory) ctx.lookup(hibernateSessionFactory);
            hsession = hfactory.openSession();
            Criteria crit1 = hsession.createCriteria(Roles.class);
            List roles = crit1.list();
            if ((roles != null) && (roles.size() > 0)) {
                return;
            } else {
                Roles guest = new Roles();
                guest.setRolName("guest");
                hsession.save(guest);
                Roles member = new Roles();
                member.setRolName("member");
                hsession.save(member);
                Roles mail = new Roles();
                mail.setRolName("mail");
                hsession.save(mail);
                Roles bookmark = new Roles();
                bookmark.setRolName("bookmark");
                hsession.save(bookmark);
                Roles chat = new Roles();
                chat.setRolName("chat");
                hsession.save(chat);
                Roles admin = new Roles();
                admin.setRolName("admin");
                hsession.save(admin);
                hsession.flush();
                Criteria crit2 = hsession.createCriteria(Users.class);
                crit2.add(Restrictions.eq("useUsername", "duroty"));
                Users duroty = (Users) crit2.uniqueResult();
                if (duroty == null) {
                    duroty = new Users();
                    duroty.setUseActive(true);
                    duroty.setUseEmail("duroty@localhost");
                    duroty.setUseLanguage("en");
                    duroty.setUseName("Duroty System");
                    duroty.setUsePassword("duroty");
                    duroty.setUseRegisterDate(new Date());
                    duroty.setUseUsername("duroty");
                    UserRole userRole1 = new UserRole(new UserRoleId(duroty, member));
                    UserRole userRole2 = new UserRole(new UserRoleId(duroty, bookmark));
                    UserRole userRole3 = new UserRole(new UserRoleId(duroty, chat));
                    UserRole userRole4 = new UserRole(new UserRoleId(duroty, admin));
                    duroty.addUserRole(userRole1);
                    duroty.addUserRole(userRole2);
                    duroty.addUserRole(userRole3);
                    duroty.addUserRole(userRole4);
                    MailPreferences mailPreferences = new MailPreferences();
                    mailPreferences.setMaprHtmlMessage(true);
                    mailPreferences.setMaprMessagesByPage(20);
                    mailPreferences.setMaprQuotaSize(1048576);
                    mailPreferences.setUsers(duroty);
                    Identity identity = new Identity();
                    identity.setIdeActive(true);
                    identity.setIdeCode(RandomStringUtils.randomAlphanumeric(25));
                    identity.setIdeDefault(true);
                    identity.setIdeEmail("duroty@localhost");
                    identity.setIdeName("Duroty System");
                    identity.setIdeReplyTo("duroty@localhost");
                    identity.setUsers(duroty);
                    HashSet set = new HashSet();
                    set.add(mailPreferences);
                    duroty.setMailPreferenceses(set);
                    HashSet identities = new HashSet();
                    identities.add(identity);
                    duroty.setIdentities(identities);
                    hsession.save(duroty);
                    hsession.flush();
                }
                Criteria crit3 = hsession.createCriteria(Users.class);
                crit3.add(Restrictions.eq("useUsername", "guest"));
                Users userGuest = (Users) crit3.uniqueResult();
                if (userGuest == null) {
                    userGuest = new Users();
                    userGuest.setUseActive(true);
                    userGuest.setUseEmail("guest@localhost");
                    userGuest.setUseLanguage("en");
                    userGuest.setUseName("Guest System");
                    userGuest.setUsePassword("guest");
                    userGuest.setUseRegisterDate(new Date());
                    userGuest.setUseUsername("guest");
                    UserRole userRole1 = new UserRole(new UserRoleId(userGuest, guest));
                    ;
                    userGuest.addUserRole(userRole1);
                    MailPreferences mailPreferences = new MailPreferences();
                    mailPreferences.setMaprHtmlMessage(true);
                    mailPreferences.setMaprMessagesByPage(20);
                    mailPreferences.setMaprQuotaSize(1048576);
                    mailPreferences.setUsers(userGuest);
                    HashSet set = new HashSet();
                    set.add(mailPreferences);
                    duroty.setMailPreferenceses(set);
                    hsession.save(userGuest);
                    hsession.flush();
                }
            }
        } catch (Exception e) {
            System.gc();
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            DLog.log(DLog.ERROR, this.getClass(), writer.toString());
        } catch (OutOfMemoryError e) {
            System.gc();
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            DLog.log(DLog.ERROR, this.getClass(), writer.toString());
        } catch (Throwable e) {
            System.gc();
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            DLog.log(DLog.ERROR, this.getClass(), writer.toString());
        } finally {
            GeneralOperations.closeHibernateSession(hsession);
            System.gc();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized boolean isInit() {
        synchronized (InitServerTask.class) {
            return this.init;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param init DOCUMENT ME!
     */
    public synchronized void setInit(boolean init) {
        synchronized (InitServerTask.class) {
            this.init = init;
        }
    }
}
