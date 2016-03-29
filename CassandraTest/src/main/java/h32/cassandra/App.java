package h32.cassandra;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;

public class App {

    public static void main(String[] args) {

        System.out.println("Hello World!");

        try (Cluster cluster = Cluster.builder().addContactPoint("cassandra").build()) {

            Metadata metadata = cluster.getMetadata();
            System.out.printf("Connected to cluster: %s\n", metadata.getClusterName());
            for (Host host : metadata.getAllHosts()) {
                System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
                        host.getDatacenter(), host.getAddress(), host.getRack());
            }
            for (KeyspaceMetadata keyspaceMetadata : metadata.getKeyspaces()) {
                for (TableMetadata tableMetadata : keyspaceMetadata.getTables()) {
                    System.out.printf("Keyspace: %s; Table: %s\n",
                            keyspaceMetadata.getName(), tableMetadata.getName());
                    Collection<IndexMetadata> indexes = tableMetadata.getIndexes();
                    for (IndexMetadata index : indexes) {
                        System.out.printf("Keyspace: %s; Table: %s, Index: %s\n",
                                keyspaceMetadata.getName(), tableMetadata.getName(), index.getName());
                    }

                }
                for (MaterializedViewMetadata materializedViewMetadata : keyspaceMetadata.getMaterializedViews()) {
                    System.out.printf("Keyspace: %s; MatView: %s\n",
                            keyspaceMetadata.getName(), materializedViewMetadata.getName());
                }
            }

            try (Session session = cluster.newSession()) {

                MappingManager mappingManager = new MappingManager(session);
                Mapper<User> userMapper = mappingManager.mapper(User.class);
                UserAccessor userAccessor = mappingManager.createAccessor(UserAccessor.class);

                User user = userMapper.get(UUID.fromString("9c04abb0-f2d3-11e5-8519-a1b761fecc63"));
                long startTime = System.currentTimeMillis();
                user = userMapper.get(UUID.fromString("06eed462-f6ec-4c55-8090-0d7351aa4b8e"));
                System.out.println(user);
                long execTime = System.currentTimeMillis() - startTime;
                System.out.printf("get by PK %s ms\n", execTime);

                User usr_98236 = userAccessor.getByUsernameIndex("usr_18236");
                startTime = System.currentTimeMillis();
                usr_98236 = userAccessor.getByUsernameIndex("usr_98246");
                usr_98236 = userAccessor.getByUsernameIndex("usr_98236");
                execTime = System.currentTimeMillis() - startTime;
                System.out.println(usr_98236);
                System.out.printf("get by Index %s ms\n", execTime);

                User usr_98237 = userAccessor.getByUsernameMatView("usr_18237");
                startTime = System.currentTimeMillis();
                usr_98237 = userAccessor.getByUsernameMatView("usr_98247");
                usr_98237 = userAccessor.getByUsernameMatView("usr_98237");
                execTime = System.currentTimeMillis() - startTime;
                System.out.println(usr_98237);
                System.out.printf("get by MatView %s ms\n", execTime);

                int prefix = new Random().nextInt();
                user.setLoginCount(new Random().nextInt());
                userMapper.save(user);

                startTime = System.currentTimeMillis();
                for (int i = 0; i < 1000; i++) {
                    User newUser = new User();
                    newUser.setUserid(UUID.randomUUID());
                    newUser.setUsername("usr_" + i + "_" + prefix);
                    newUser.setPassword("start");
                    newUser.setLoginCount(-1);
                    userMapper.saveAsync(newUser);
                }
                session.close();
                execTime = System.currentTimeMillis() - startTime;
                System.out.printf("1000 inserts %s ms\n", execTime);
            }
        }
    }
}
