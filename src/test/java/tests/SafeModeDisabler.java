package tests;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

public class SafeModeDisabler {

    private static final Logger log = LoggerFactory.getLogger(SafeModeDisabler.class);

    public static void main(String[] args) {
        // 🔧 Replace these values with your actual credentials/config
        String sshUser = System.getenv("SSH_USER");
        String sshPassword = System.getenv("SSH_PASSWORD");
        String sshHost = System.getenv("SSH_HOST");
        int sshPort = Integer.parseInt(System.getenv("SSH_PORT"));

        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");
        String dbName = System.getenv("DB_NAME");
        String remoteHost = System.getenv("DB_REMOTE_HOST");
        int remotePort = Integer.parseInt(System.getenv("DB_REMOTE_PORT"));
        int localPort = Integer.parseInt(System.getenv("DB_LOCAL_PORT"));

        String phoneNumber = "0860539646"; // 📱 phone number to use in WHERE clause

        disableSafeMode(sshUser, sshHost, sshPort, sshPassword,
                dbUser, dbPassword, dbName,
                phoneNumber, remoteHost, remotePort, localPort);
    }

    public static void disableSafeMode(String sshUser, String sshHost, int sshPort, String sshPassword,
                                       String dbUser, String dbPassword, String dbName,
                                       String phoneNumber, String remoteHost, int remotePort, int localPort) {

        Session session = null;

        try {
            log.info("Starting SSH session to {}", sshHost);

            // Establish SSH tunnel
            JSch jsch = new JSch();
            session = jsch.getSession(sshUser, sshHost, sshPort);
            session.setPassword(sshPassword);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            session.setPortForwardingL(localPort, remoteHost, remotePort);
            log.info("SSH tunnel established: localhost:{} -> {}:{}", localPort, remoteHost, remotePort);

            // PostgreSQL connection
            String jdbcUrl = "jdbc:postgresql://localhost:" + localPort + "/" + dbName;
            Properties props = new Properties();
            props.setProperty("user", dbUser);
            props.setProperty("password", dbPassword);

            try (Connection conn = DriverManager.getConnection(jdbcUrl, props)) {
                log.info("Connected to PostgreSQL database: {}", dbName);

                String sql = """
                        UPDATE device
                        SET safemode = false
                        WHERE id IN (
                            SELECT id FROM device
                            WHERE phoneNumber = ?
                              AND active = true
                              AND safemode = true
                            ORDER BY createddate DESC
                            LIMIT 1
                        );
                        """;

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, phoneNumber);
                    int rows = stmt.executeUpdate();
                    log.info("Query executed. Rows updated: {}", rows);
                }
            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage(), e);
        } finally {
            if (session != null && session.isConnected()) {
                session.disconnect();
                log.info("SSH session closed.");
            }
        }
    }
}