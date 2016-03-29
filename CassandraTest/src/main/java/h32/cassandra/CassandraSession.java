package h32.cassandra;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

import java.io.Closeable;

public class CassandraSession implements Closeable{
    private Session session;
    MappingManager mappingManager;


    public CassandraSession(Session session) {
        this.session = session;
        this.mappingManager = new MappingManager(session);
    }

    @Override
    public void close() {
        session.close();
    }

    public Mapper<User> getUserMappper() {
        //TODO lazy and cache
        return mappingManager.mapper(User.class);
    }

    public UserAccessor getUserAccessor() {
        //TODO lazy and cache
        return mappingManager.createAccessor(UserAccessor.class);
    }
}
