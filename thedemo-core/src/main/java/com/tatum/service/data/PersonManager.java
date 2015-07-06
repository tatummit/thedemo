package com.tatum.service.data;

import com.tatum.model.Person;

public interface PersonManager {

    Person getId(long id);

    Person insert(Person person);

    int update(Person person);

}
