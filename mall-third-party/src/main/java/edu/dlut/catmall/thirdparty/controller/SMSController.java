package edu.dlut.catmall.thirdparty.controller;

import edu.dlut.catmall.thirdparty.component.SMSComponent;
import edu.dlut.common.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/17  17:26
 * DESCRIPTION:
 **/
@RequestMapping("/sms")
@RestController
public class SMSController {

    @Resource
    private SMSComponent smsComponent;

    @GetMapping("/send")
    public R sendSMSCode(@RequestParam("phone") String phone, @RequestParam("code") String code) {
        smsComponent.sendSMSCode(phone, code);
        return R.ok();
    }
}
