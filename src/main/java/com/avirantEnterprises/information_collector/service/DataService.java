package com.avirantEnterprises.information_collector.service;

import com.avirantEnterprises.information_collector.model.Data;

public interface DataService {
    void saveData(Data data);
    Iterable<Data> findAllData();
    Data getDataById(Long id);
    void deleteData(Long id);
}
