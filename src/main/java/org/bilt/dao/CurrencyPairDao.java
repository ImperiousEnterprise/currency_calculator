package org.bilt.dao;

import org.bilt.models.CurrencyPair;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class CurrencyPairDao implements DAO<CurrencyPair>{
    private ConcurrentHashMap<String,ConcurrentHashMap<String, Double>> pairs = new ConcurrentHashMap<>();

    @Override
    public Map<String, Double> getAllRatesForACurrency(String base) {
        return this.pairs.get(base);
    }

    @Override
    public List<String> getAllCurrencies() {
        return new ArrayList<>(this.pairs.keySet());
    }

    @Override
    public double getRate(String base, String quote) {
        return this.pairs.get(base).get(quote);
    }

    @Override
    public boolean pairingExists(String base, String quote) {
        if(!this.pairs.containsKey(base)){
            return false;
        }else return this.pairs.get(base).containsKey(quote);
    }

    @Override
    public void save(CurrencyPair currencyPair) {
        if(this.pairs.containsKey(currencyPair.base)){
            this.pairs.get(currencyPair.base).put(currencyPair.quote, currencyPair.rate);
        }else{
            ConcurrentHashMap<String,Double> subtree = new ConcurrentHashMap<>();
            subtree.put(currencyPair.quote, currencyPair.rate);
            this.pairs.put(currencyPair.base, subtree);
        }
    }

}