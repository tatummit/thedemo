package com.tatum.util;

import org.springframework.jdbc.core.SqlParameterValue;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class DAOUtils {

    private DAOUtils() {

    }

    public static PreparedStatement prepareSetNumber(int index, PreparedStatement ps, Long data, int dataType)
            throws SQLException {

        if (data == null) {
            ps.setNull(index, dataType);
        } else {
            ps.setLong(index,data);
        }
        return ps;
    }

    public static PreparedStatement prepareSetNumber(int index, PreparedStatement ps, Integer data, int dataType)
            throws SQLException {

        if (data == null) {
            ps.setNull(index, dataType);
        } else {
            ps.setInt(index,data);
        }
        return ps;
    }

    public static SqlParameterValue sqlParameterFromNumber(Long value){
        if(value == null){
            return new SqlParameterValue(Types.NULL, null);
        } else {
            return new SqlParameterValue(Types.BIGINT, value);
        }
    }
}
