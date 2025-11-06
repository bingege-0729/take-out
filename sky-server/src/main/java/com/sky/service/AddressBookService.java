package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    //查询用户所有地址
    List<AddressBook> list(AddressBook addressBook);
    //新增地址
    void save(AddressBook addressBook);
    //根据id查询地址
    AddressBook getById(Long id);
    //修改地址
    void update(AddressBook addressBook);
    //
    void setDefault(AddressBook addressBook);
    //删除地址
    void deleteById(Long id);
}
