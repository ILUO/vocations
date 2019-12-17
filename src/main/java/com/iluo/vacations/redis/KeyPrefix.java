package com.iluo.vacations.redis;

public interface KeyPrefix {

    public int expireSeconds() ;

    public String getPrefix() ;

}
