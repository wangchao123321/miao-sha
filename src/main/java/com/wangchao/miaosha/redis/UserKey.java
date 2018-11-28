package com.wangchao.miaosha.redis;

public class UserKey extends BasePrefix {

    private UserKey(String prefix) {
        super(0,prefix);
    }

    private UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
}
