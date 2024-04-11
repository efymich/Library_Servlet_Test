package org.efymich.myapp.utils;

import java.util.Map;

public class Util {
    public static Map<String, Object> calculatePagination(Long count,Integer currentPage) {
        int countOfPages = (int) Math.ceil(count/(double) Constants.RECORDS_PER_PAGE);
        int current = currentPage + 3;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 6, countOfPages);
        return Map.of("beginIndex", begin, "endIndex", end);
    }
}
