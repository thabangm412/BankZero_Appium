package factory;

import models.DbUserData;
import models.ScheduleTransferData;
import models.TransferData;
import models.User;

public class TransferDataFactory {

//    public static TransferData validTransfer() {
//
//        User user = new User("Isagi", "33333");
//
//        return new TransferData(
//                user,
//                "Savings Test",
//                "30",
//                "Auto Test"
//        );
//    }

    // Based on the @Builder and @Value implementation on the ScheduleTransferData class

    public static TransferData validTransfer()
    {
        return TransferData.builder()
                .user(new User("Isagi", "33333"))
                .accountName("Savings Test")
                .amount("30")
                .ref("Auto Test")
                .build();
    }

    public static DbUserData validDbUserData() {

        return DbUserData.builder()
                .sshUser(System.getenv("SSH_USER"))
                .sshPassword(System.getenv("SSH_PASSWORD"))
                .sshHost(System.getenv("SSH_HOST"))
                .sshPort(Integer.parseInt(System.getenv("SSH_PORT")))
                .dbUser(System.getenv("DB_USER"))
                .dbPassword(System.getenv("DB_PASSWORD"))
                .dbName(System.getenv("DB_NAME"))
                .remoteHost(System.getenv("DB_REMOTE_HOST"))
                .remotePort(Integer.parseInt(System.getenv("DB_REMOTE_PORT")))
                .localPort(Integer.parseInt(System.getenv("DB_LOCAL_PORT")))
                .build();
    }
    public static ScheduleTransferData onceOffTransfer() {

        User user = new User("Isagi", "33333");

        return new ScheduleTransferData(
                user,
                "Savings Test",
                "30",
                "Auto Test",
                "Once-off",
                2
        );
    }

    public static ScheduleTransferData weeklyTransfer() {

        User user = new User("Isagi", "33333");

        return new ScheduleTransferData(
                user,
                "Savings Test",
                "30",
                "Auto Test",
                "Weekly",
                2
        );
    }

    public static ScheduleTransferData monthlyTransfer() {

        User user = new User("Isagi", "33333");

        return new ScheduleTransferData(
                user,
                "Savings Test",
                "30",
                "Auto Test",
                "Monthly",
                2
        );
    }
}