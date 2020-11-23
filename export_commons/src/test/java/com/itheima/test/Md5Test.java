package com.itheima.test;

import org.apache.shiro.crypto.hash.Md5Hash;

public class Md5Test {

    public static void main(String[] args) {
        //1.原密码  2.盐值  3.散列(加密)次数
        String s = new Md5Hash("123", "laowang@export.com", 2).toString();
        System.out.println(s);
    }
}
