package com.tatum.service.data.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import com.tatum.service.data.PersonManager;
import com.tatum.model.Person;
import com.tatum.util.DAOUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

@Repository
@Service
@Qualifier("PersonDAO")
public class PersonMySQLDAO implements PersonManager{

    JdbcTemplate jdbcTemplate;

    @Autowired
    void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    static private String COLUMN = "firstname, lastname, age, phone";

    @Override
    public Person getId(long id) {
        try {
            String sql = "SELECT id, " + COLUMN + " FROM person WHERE id = ? ";
            return jdbcTemplate.queryForObject(sql, new PersonModelMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    @Override
    public Person insert(Person person) {
        KeyHolder key = new GeneratedKeyHolder();
        jdbcTemplate.update(new PersonInsertPs(person),key);
        long retId = key.getKey().longValue();
        person.setId(retId);
        return person;
    }

    @Override
    public int update(Person person) {
        String sql = "UPDATE person "+
                     "SET firstname = ?, lastname = ?, age = ?, phone = ? " +
                     "WHERE id = ?";
        return jdbcTemplate.update(sql,person.getFirstname(), person.getLastname()
                , person.getAge(),person.getPhone(),person.getId());
    }

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

            String sql = "INSERT INTO person (" +
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
