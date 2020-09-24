package org.bilt.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DAO<T> {

    Map<String,Double> getAllRatesForACurrency(String base);
    List<String> getAllCurrencies();

    double getRate(String base, String quote);

    boolean pairingExists(String base, String quote);
    void save(T t);

}
