package DbQueries;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import factory.TransferDataFactory;
import models.DbUserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class EmailsConfig {

    private static final Logger log =
            LoggerFactory.getLogger(EmailsConfig.class);

    private static final String EMAIL_PROPERTY =
            "send.email.in.test.environment";

    public static void enableEmails() throws JSchException {
        updateEmailFlag(true);
    }

    public static void disableEmails() throws JSchException {
        updateEmailFlag(false);
    }

    // =========================
    // CORE LOGIC
    // =========================

    private static void updateEmailFlag(boolean enable)
            throws JSchException {

        DbUserData dbData =
                TransferDataFactory.validDbUserData();

        Session session = null;

        try {
            log.info("Starting SSH session to {}",
                    dbData.getSshHost());

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

            log.info("SSH tunnel established");

            String jdbcUrl =
                    "jdbc:postgresql://localhost:"
                            + dbData.getLocalPort()
                            + "/"
                            + dbData.getDbName();

            Properties props = new Properties();
            props.setProperty("user", dbData.getDbUser());
            props.setProperty("password", dbData.getDbPassword());

            try (Connection conn =
                         DriverManager.getConnection(jdbcUrl, props)) {

                log.info("Connected to DB");

                String selectSql = """
                        SELECT value
                        FROM public.system_property
                        WHERE name = ?
                        """;

                try (PreparedStatement selectStmt =
                             conn.prepareStatement(selectSql)) {

                    selectStmt.setString(1, EMAIL_PROPERTY);

                    try (ResultSet rs =
                                 selectStmt.executeQuery()) {

                        if (rs.next()) {

                            boolean currentValue =
                                    rs.getBoolean("value");

                            if (currentValue == enable) {

                                log.info(
                                        "Email flag already set to {}",
                                        enable
                                );
                                return;
                            }
                        }
                    }
                }

                String updateSql = """
                        UPDATE public.system_property
                        SET value = ?
                        WHERE name = ?
                        """;

                try (PreparedStatement updateStmt =
                             conn.prepareStatement(updateSql)) {

                    updateStmt.setBoolean(1, enable);
                    updateStmt.setString(2, EMAIL_PROPERTY);

                    updateStmt.executeUpdate();

                    log.info("Email flag updated to {}",
                            enable);
                }
            }

        } catch (Exception e) {

            log.error("Error updating email flag: {}",
                    e.getMessage(), e);

        } finally {

            if (session != null && session.isConnected()) {
                session.disconnect();
                log.info("SSH session closed");
            }
        }
    }
}