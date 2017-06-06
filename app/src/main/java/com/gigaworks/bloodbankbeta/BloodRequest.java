package com.gigaworks.bloodbankbeta;

/**
 * Created by Arch on 15-04-2017.
 */

public class BloodRequest {
    private String name,reqId,phone,age,hospital;

    public BloodRequest(){ }

    public BloodRequest(String name,String phone,String hospital,String age){
        this.age=age;
        this.hospital=hospital;
        this.name=name;
        this.phone=phone;
    }

    public void setReqId(String reqId){
        this.reqId=reqId;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setPhone(String phone){
        this.phone=phone;
    }
    public void setAge(String age){
        this.age=age;
    }
    public void setHospital(String hospital){
        this.hospital=hospital;
    }

    public String getName(){return name; }

    public String getPhone() {
        return phone;
    }

    public String getAge() {
        return age;
    }

    public String getHospital() {
        return hospital;
    }

    public String getReqId() {
        return reqId;
    }
}
