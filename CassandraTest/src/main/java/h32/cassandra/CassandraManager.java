package h32.cassandra;

import com.datastax.driver.core.*;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;

public class CassandraManager implements Closeable{

    private Cluster clusterInstance;

    public Cluster getCluster() {
        if (clusterInstance == null) {
            clusterInstance = Cluster.builder().addContactPoint("cassandra").build();
        }
        return clusterInstance;
    }

    public void printMetadata() {
        Metadata metadata = getCluster().getMetadata();
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
    }

    @Override
    public void close() {
        clusterInstance.close();
    }

    public CassandraSession newSession() {
        return new CassandraSession(clusterInstance.newSession());
    }
}
