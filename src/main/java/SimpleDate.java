public class SimpleDate {

    private final int DAY = 2;
    private final int MONTH = 1;
    private final int YEAR = 0;


    private int day;
    private int month;
    private int year;


    public SimpleDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public SimpleDate(String date) {
        String[] parsedDate = date.split("-");
        this.day = Integer.parseInt(parsedDate[DAY]);
        this.month = Integer.parseInt(parsedDate[MONTH]);
        this.year = Integer.parseInt(parsedDate[YEAR]);
    }



    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return  String.format("%s.%s.%s",this.day,this.month,this.year);
    }
}
