package br.gov.framework.demoiselle.persistence.JDBC;

import java.sql.SQLException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import br.gov.framework.demoiselle.core.context.ContextLocator;
import br.gov.framework.demoiselle.persistence.hibernate.Professor;

public class JDBCGenericDAOTst {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testFindString() throws SQLException {
        JDBCMock mock = new JDBCMock();
        mock.configure();
        ContextLocator.getInstance().setTransactionContext(mock.getContextMock());
        JDBCUtil util = JDBCUtil.getInstance();
        util.beginTransaction(mock.getConnectionMock());
        JDBCGenericDAO<Professor> dao = new JDBCDAO<Professor>();
        dao.find("select  * from student");
    }

    @Test
    public void testFindStringClassOfA() {
        JDBCMock mock = new JDBCMock();
        mock.configure();
        ContextLocator.getInstance().setTransactionContext(mock.getContextMock());
        JDBCUtil util = JDBCUtil.getInstance();
        util.beginTransaction(mock.getConnectionMock());
        JDBCGenericDAO<Professor> dao = new JDBCDAO<Professor>();
        dao.find("from aluno ", Professor.class);
    }

    @Test
    public void testExecute() {
        JDBCMock mock = new JDBCMock();
        mock.configure();
        ContextLocator.getInstance().setTransactionContext(mock.getContextMock());
        JDBCUtil util = JDBCUtil.getInstance();
        util.beginTransaction(mock.getConnectionMock());
        JDBCGenericDAO<Professor> dao = new JDBCDAO<Professor>();
        int atualizacoes = dao.execute("update student set id=1,name='test' ");
        Assert.assertEquals(1, atualizacoes);
    }

    @Test
    public void testGetColumnNamesFromSQL() {
        JDBCMock mock = new JDBCMock();
        mock.configure();
        ContextLocator.getInstance().setTransactionContext(mock.getContextMock());
        JDBCGenericDAO<Professor> dao = new JDBCDAO<Professor>();
        String[] columns = dao.getColumnNamesFromSQL(" select name,id from student ");
        Assert.assertEquals("name", columns[0]);
        Assert.assertEquals("id", columns[1]);
        Assert.assertTrue(columns.length > 0);
    }

    @Test
    public void testGetTableName() {
        JDBCMock mock = new JDBCMock();
        mock.configure();
        ContextLocator.getInstance().setTransactionContext(mock.getContextMock());
        JDBCUtil util = JDBCUtil.getInstance();
        util.beginTransaction(mock.getConnectionMock());
        JDBCGenericDAO<Professor> dao = new JDBCDAO<Professor>();
        String name = dao.getTableName(" select name,id from student ");
        Assert.assertEquals("student", name);
    }
}
