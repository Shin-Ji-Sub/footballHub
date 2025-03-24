package com.mezzala.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParsing {
    private static class TIME_MAXIMUM {
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
    }

    public String calculateTime(Date date) {
        long curTime = System.currentTimeMillis();
        long regTime = date.getTime();
        long diffTime = (curTime - regTime) / 1000;
        String msg = null;
        if (diffTime < TIME_MAXIMUM.SEC) {
            // sec
            msg = diffTime + "초 전";
        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
            // min
            msg = diffTime + "분 전";
        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
            // hour
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < 7) {
            // day
            msg = (diffTime) + "일 전";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
            msg = sdf.format(date);
        }
        return msg;
    }
}
