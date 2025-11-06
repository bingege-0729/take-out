package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressBookMapper {

    //查询所有 地址
    @Select("select * from address_book where user_id = #{userId}")
    void findAllAddressBook(AddressBook addressBook);


    //添加 地址
    List<AddressBook> list(AddressBook addressBook);


    //修改 地址
    @Insert("insert into address_book" +
            "(user_id,consignee,phone,sex,province_code,city_code,city_name" +
            ",district_code,district_name,detail,label,is_default)" +
            "values(#{userId},#{consignee},#{phone},#{sex},#{provinceCode},#{cityCode},#{cityName}," +
            "#{districtCode},#{districtName},#{detail},#{label},#{isDefault})")
    void insert(AddressBook addressBook);




    //根据id查询 地址
    @Select("select * from address_book where id = #{id}")
    AddressBook getById(Long id);

    //修改 地址
    void update(AddressBook addressBook);

    @Update("update address_book set is_default = #{isDefault} where user_id = #{userId}")
    void updateIsDefaultByUserId(AddressBook addressBook);

    //删除 地址
    @Delete("delete from address_book where id = #{id}")
    void deleteById(Long id);
}
