package factory;

import models.*;

public class TransferDataFactory {

    public static CardData validCardData() {
        return CardData.builder()
                .cardPin("11111")
                .cardNumber("2265 0833 8894 8418")
                .build();
    }
    public static TransferData validTransfer()
    {
        return TransferData.builder()
                .user(new User("Isagi", "33333"))
                .accountName("Savings Test")
                .amount("30")
                .ref("Auto Test")
                .build();
    }

    public static TransferData validBusinessTransfer()
    {
        return TransferData.builder()
                .user(new User("Isagi", "33333"))
                .accountName("Business")
                .amount("30")
                .ref("Auto Test")
                .build();
    }

    public static TransferData valid7daysNoticeTransfer()
    {
        return TransferData.builder()
                .user(new User("Isagi", "33333"))
                .accountName("7 Days Notice")
                .amount("30")
                .ref("Auto Test")
                .build();
    }

    public static TransferData valid32daysNoticeTransfer()
    {
        return TransferData.builder()
                .user(new User("Isagi", "33333"))
                .accountName("32 Days Notice")
                .amount("30")
                .ref("Auto Test")
                .build();
    }

    public static TransferData valid45daysNoticeTransfer()
    {
        return TransferData.builder()
                .user(new User("Isagi", "33333"))
                .accountName("45 Days Notice")
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

    public static User validAppUser() {
        return new User("Isagi", "33333");
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