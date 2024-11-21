package com.avirantEnterprises.information_collector.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.avirantEnterprises.information_collector.model.Data;
import com.avirantEnterprises.information_collector.repository.DataRepository;

@Service
public class DataServiceImpl implements DataService {
    @Autowired
    private DataRepository dataRepository;

    @Override
    public void saveData(Data data) {
        dataRepository.save(data);
    }

    @Override
    public Iterable<Data> findAllData() {
        return dataRepository.findAll();
    }

    @Override
    public Data getDataById(Long id) {
        return dataRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteData(Long id) {
        dataRepository.deleteById(id);
    }
}
