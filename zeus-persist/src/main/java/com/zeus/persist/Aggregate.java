package com.zeus.persist;

import java.util.List;

public interface Aggregate<T> {

    Number getAvg(String proName, T t, int begin, int length);

    Number getMid(String proName, T t, int begin, int length);

    Number getMax(String proName, T t, int begin, int length);

    Number getMin(String proName, T t, int begin, int length);

    <E> List<E> getDistinctProValue(String proName, T t, int begin, int length,Class<E> clazz);

    <E> List<E> getList4ProValue(String proName, T t, int begin, int length,Class<E> clazz);

    //GroupResult group(T t,List<String> groups,List<String> indicators);

}
