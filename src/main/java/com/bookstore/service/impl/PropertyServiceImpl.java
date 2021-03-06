package com.bookstore.service.impl;

import com.bookstore.bean.Property;
import com.bookstore.dao.PropertyDao;
import com.bookstore.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ${邹} on 2018/9/19.
 */
@Service
public class PropertyServiceImpl implements PropertyService {
    @Autowired
    private PropertyDao propertyDao;
    @Override
    public List<Property> select() {
        return propertyDao.select();
    }

    @Override
    public int deleteById(Long id) {
        return propertyDao.deleteById( id );
    }

    @Override
    public int updateById(Property property) {
        return propertyDao.updateById( property );
    }

    @Override
    public int insert(Property property) {
        return propertyDao.insert( property );
    }

}
