package model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.sql.Date;

import util.DateBuilder;

public class InstantDateConverter implements DynamoDBTypeConverter<String, Date> {
    @Override
    public String convert(Date date) {
        return date.toString();
    }

    @Override
    public Date unconvert(String s) {
        return DateBuilder.getDate(s);
    }
}
