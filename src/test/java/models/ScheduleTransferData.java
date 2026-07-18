package models;

import utils.AppiumUtils;

public class ScheduleTransferData {

    private final User user;
    private final String accountName;
    private final String amount;
    private final String ref;
    private final String scheduleType;
    private final int daysToAdd;

    public ScheduleTransferData(User user,
                                String accountName,
                                String amount,
                                String ref,
                                String scheduleType,
                                int daysToAdd) {

        this.user = user;
        this.accountName = accountName;
        this.amount = amount;
        this.ref = ref;
        this.scheduleType = scheduleType;
        this.daysToAdd = daysToAdd;
    }

    public User getUser() {
        return user;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAmount() {
        return amount;
    }

    public String getRef() {
        return ref;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public int getDaysToAdd() {
        return daysToAdd;
    }

    public String getFormattedAmount() {

        double parsedAmount =
                Double.parseDouble(amount);

        return String.format("R%.2f", parsedAmount);
    }

    public String getFutureDate() {

        return AppiumUtils.getFutureDate(daysToAdd);
    }

    public String getScheduleTypeLowerCase() {

        return scheduleType.toLowerCase();
    }
}