package com.ptv.livebox.movie.common.utils;

import java.time.OffsetDateTime;
import java.util.Date;

public class DateTimeUtils {
    public static Date parse(String value) {
        return new Date(OffsetDateTime.parse(value)
                .toInstant()
                .toEpochMilli());
    }
}
