package app.sporttime.model;

/**
 * Created by timo on 06.12.16.
 */

public class Record {
    int record_id;
    int value;
    int datetime;
    int sports_sport_id;

    public int getRecord_id() {
        return record_id;
    }

    public Float getValue() {
        return Float.valueOf(value);
    }
    public int getIntDatetime() {
        return datetime;
    }
    public Float getFloatDatetime() {
        return Float.valueOf(datetime);
    }

    public int getSports_sport_id() {
        return sports_sport_id;
    }
}
