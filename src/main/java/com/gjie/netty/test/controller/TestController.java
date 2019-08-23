package com.gjie.netty.test.controller;

import com.gjie.netty.annotion.HttpWeb;
import com.gjie.netty.annotion.RequestBody;
import com.gjie.netty.annotion.RequestMapping;
import com.gjie.netty.annotion.ResponseBody;
import com.gjie.netty.test.bo.TestReq;
import com.gjie.netty.test.bo.TestResp;

/**
 * @author j_gong
 * @date 2019/8/23 3:12 PM
 */
@HttpWeb
public class TestController {

    @RequestMapping(url="/test")
    public @ResponseBody TestResp test(@RequestBody TestReq testReq){
        Integer age = testReq.getAge()+1;
        TestResp testResp = new TestResp();
        testResp.setAge(age);
        testResp.setSex(testReq.getSex());
        testResp.setName(testReq.getName());
        return testResp;
    }
}
