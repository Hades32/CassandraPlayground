package h32.cassandra;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

@Table(keyspace = "H32Data", name = "user")
public class User {

    @PartitionKey
    private UUID userid;
    @Column(name = "login_count")
    private int loginCount;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;

    public UUID getUserid() {
        return userid;
    }

    public void setUserid(UUID userid) {
        this.userid = userid;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "userid=" + userid +
                ", loginCount=" + loginCount +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
