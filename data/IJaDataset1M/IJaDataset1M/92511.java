package org.jacuzzi;

import org.jacuzzi.core.GenericDaoImpl;
import javax.sql.DataSource;
import java.util.List;

/**
 * @author Mike Mirzayanov
 */
public class UserDaoImpl extends GenericDaoImpl<User, Long> implements UserDao {

    protected UserDaoImpl(DataSource source) {
        super(source);
    }

    @Override
    public List<User> findByName(String name) {
        return findBy("name=?", name);
    }

    @Override
    public User findOnlyByName(String name) {
        return findOnlyBy(true, "name=?", name);
    }
}
