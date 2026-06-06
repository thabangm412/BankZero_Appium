package DbQueries;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import factory.TransferDataFactory;
import models.DbUserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Properties;

public class DbConfig {

    private static final Logger log =
            LoggerFactory.getLogger(DbConfig.class);

    public static boolean customerExists(String registrationNumber)
            throws JSchException {

        DbUserData dbData = TransferDataFactory.validDbUserData();
        Session session = null;

        String query = """
        SELECT EXISTS (
            SELECT 1
            FROM customer
            WHERE comanyregistrationnumber = ?
        )
        """;

        try {
            JSch jsch = new JSch();

            session = jsch.getSession(
                    dbData.getSshUser(),
                    dbData.getSshHost(),
                    dbData.getSshPort()
            );

            session.setPassword(dbData.getSshPassword());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            session.setPortForwardingL(
                    dbData.getLocalPort(),
                    dbData.getRemoteHost(),
                    dbData.getRemotePort()
            );

            String jdbcUrl =
                    "jdbc:postgresql://localhost:"
                            + dbData.getLocalPort()
                            + "/"
                            + dbData.getDbName();

            Properties props = new Properties();
            props.setProperty("user", dbData.getDbUser());
            props.setProperty("password", dbData.getDbPassword());

            try (Connection conn =
                         DriverManager.getConnection(jdbcUrl, props);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, registrationNumber);

                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next() && rs.getBoolean(1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database query failed", e);
        } finally {
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }
}