package org.bilt.service;


import org.bilt.models.CurrencyPair;
import org.bilt.dao.CurrencyPairDao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class CurrencyPairService {

    @Inject
    CurrencyPairDao currencyPairDao;

    public void save(CurrencyPair currencyPair){
        currencyPairDao.save(currencyPair);
    }

    public List<String> getAllCurrencies(){
        return currencyPairDao.getAllCurrencies();
    }

    public Map<String, Double> getAllforSpecificCurrency(String curr){
        return currencyPairDao.getAllRatesForACurrency(curr);
    }
    public Double getRate(String from, String to){
       return currencyPairDao.getRate(from, to) ;
    }

    public Boolean checkIfPairingExists(String from, String to){
        return currencyPairDao.pairingExists(from, to) ;
    }

}
