package tatum.app.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import tatum.app.model.Person;
import tatum.app.util.DAOUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

public class PersonDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    static private String COLUMN = "firstname, lastname, age, phone";

    private static class PersonModelMapper implements RowMapper<Person> {

        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            Person person = new Person();
            person.setId(rs.getLong("id"));
            person.setFirstname(rs.getString("firstname"));
            person.setLastname(rs.getString("lastname"));
            person.setAge(rs.getInt("age"));
            person.setPhone(rs.getString("phone"));
            return person;
        }
    }

    private static final class PersonInsertPs implements PreparedStatementCreator {

        private Person model;

        private PersonInsertPs(Person model) {
            this.model = model;
        }

        @Override
        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

            String sql = "INSERT INTO logline (" +
                    COLUMN + ") " +
                    "VALUES (?, ?, ?, ?)";

            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            int index = 1;
            ps.setString(index++, model.getFirstname());
            ps.setString(index++, model.getLastname());
            DAOUtils.prepareSetNumber(index++, ps, model.getAge(), Types.BIGINT);
            ps.setString(index++, model.getPhone());
            return ps;
        }
    }

}
