package com.tatum.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.tatum.exception.DemoNotFoundObjectException;
import com.tatum.service.DemoOperationCollection;
import com.tatum.service.data.BinaryManager;
import com.tatum.service.data.PersonManager;
import com.tatum.model.BinaryObject;
import com.tatum.model.Person;

@Service("DemoBackendOperation")
@Qualifier("DemoBackendOperation")
public class DemoBackController implements DemoOperationCollection{

    @Autowired
    @Qualifier("PersonDAO")
    PersonManager personManager;

    @Autowired
    BinaryManager binaryManager;

    @Override
    public Person getId(long id) {
        Person person = personManager.getId(id);
        if (person == null) {
            throw new DemoNotFoundObjectException("Cannot find metadata on personid="+person.getId());
        }
        return person;
    }

    @Override
    public Person insert(Person person) {
        return personManager.insert(person);
    }

    @Override
    public int update(Person person) {
        int eff = personManager.update(person);
        if(eff < 1) {
            throw new DemoNotFoundObjectException("Cannot find metadata on personid="+person.getId());
        }
        return eff;
    }

    @Override
    public void put(String key, BinaryObject binary) {

        Person person = personManager.getId(Long.valueOf(key));
        if (person != null) {
            binaryManager.put(key, binary);
        } else {
            throw new DemoNotFoundObjectException("Cannot find metadata on personid="+key);
        }
    }

    @Override
    public BinaryObject get(String key) {
        return binaryManager.get(key);
    }
}
