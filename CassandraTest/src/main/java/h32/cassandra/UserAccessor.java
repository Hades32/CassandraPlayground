package h32.cassandra;

import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;

@Accessor
public interface UserAccessor {
    @Query("SELECT * FROM H32Data.user_by_username WHERE username = :username")
    User getByUsernameMatView(@Param("username") String username);

    @Query("SELECT * FROM H32Data.user WHERE username = :username")
    User getByUsernameIndex(@Param("username") String username);
}
