package com.wangchao.miaosha.redis;

public class OrderKey extends BasePrefix {


    public OrderKey( String prefix) {
        super( prefix);
    }


    public static OrderKey getMiaoshUserByUidGid = new OrderKey("moug");
}
