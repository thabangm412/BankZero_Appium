package models;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DbUserData {

    String sshUser;
    String sshPassword;
    String sshHost;
    int sshPort;

    String dbUser;
    String dbPassword;
    String dbName;

    String remoteHost;
    int remotePort;
    int localPort;
}