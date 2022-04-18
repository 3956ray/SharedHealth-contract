package org.ray;


import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.hepeng.Cat;
import org.hepeng.CatQueryResultList;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.util.Random;

@Contract(
        name = "MedicalRecordContract",
        info = @Info(
            title = "MedicalRecord contract",
            description = "The hyperlegendary medicalrecord contract",
            version = "0.0.1-SNAPSHOT",
            license = @License(
                    name = "Apache 2.0 License",
                    url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
            contact = @Contact(
                    email = "f.carr@example.com",
                    name = "F Carr",
                    url = "https://hyperledger.example.com"))
)
@Default
@Log
public class MedicalRecordContract implements ContractInterface {

    //初始化账本
    @Transaction
    public void initLedger(final Context ctx) {

        ChaincodeStub stub = ctx.getStub();
        for (int i = 0; i < 10; i++ ) {
            MedicalRecord medicalRecord = new MedicalRecord()
                    .setName("patient-" + i)
                    .setAge(new Random().nextInt())
                    .setDepartment("骨科")
                    .setDiagonose("骨裂")
                    .setDoctorname("张三")
                    .setHistory("无");
            stub.putStringState(medicalRecord.getName() , JSON.toJSONString(medicalRecord));
        }

    }

    //根据key, 查找电子病例
    @Transaction
    public MedicalRecord queryMedicalRecord(final Context ctx, final String key) {

        ChaincodeStub stub = ctx.getStub();
        String recordState = stub.getStringState(key);

        if (StringUtils.isBlank(recordState)) {
            String errorMessage = String.format("MedicalRecord %s does not exist", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }

        return JSON.parseObject(recordState , MedicalRecord.class);
    }

    /*
    //通过名字查询
    @Transaction
    public MedicalRecordQueryResultList queryMedicalRecordByName(final Context ctx, String name) {

        log.info(String.format("使用 name 查询 record , name = %s" , name));

        String query = String.format("{\"selector\":{\"name\":\"%s\"} , \"use_index\":[\"_design/indexNameColorDoc\", \"indexNameColor\"]}", name);

        log.info(String.format("query string = %s" , query));
        return queryMedicalRecord(ctx.getStub(), query);
    }

    @Transaction
    public MedicalRecordPageResult queryMedicalRecordPageByName(final Context ctx, String name , Integer pageSize , String bookmark){

    }*/

    //增加一个病历本
    @Transaction
    public MedicalRecord createMedicalRecord(final Context ctx, final String key , String name , Integer age , String Date, String address, String userID, String doctorname , String department, String diagonose, String history) {

        ChaincodeStub stub = ctx.getStub();
        String recordState = stub.getStringState(key);

        //若是已经存在， 报错
        if (StringUtils.isNotBlank(recordState)) {
            String errorMessage = String.format("MedicalRecord %s already exists", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }

        //new一个病历本对象
        MedicalRecord medicalRecord = new MedicalRecord()
                .setName(name)
                .setAge(age)
                .setAddress(address)
                .setUserID(userID)
                .setDate(Date)
                .setDepartment(department)
                .setDoctorname(doctorname)
                .setDoctorname(doctorname)
                .setHistory(history)
                .setDiagonose(diagonose);

        String json = JSON.toJSONString(medicalRecord);
        //
        stub.putStringState(key, json);

        //事件
        stub.setEvent("createMedicalRecordEvent" , org.apache.commons.codec.binary.StringUtils.getBytesUtf8(json));
        return medicalRecord;
    }

    //修改数据
    @Transaction
    public MedicalRecord updateMedicalRecord(final Context ctx, final String key , String name , Integer age , String Date, String address, String userID, String doctorname , String department, String diagonose, String history) {

        ChaincodeStub stub = ctx.getStub();
        String recordState = stub.getStringState(key);

        if (StringUtils.isBlank(recordState)) {
            String errorMessage = String.format("MedicalRecord %s does not exist", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }

        MedicalRecord medicalRecord = new MedicalRecord()
                .setName(name)
                .setAge(age)
                .setAddress(address)
                .setUserID(userID)
                .setDate(Date)
                .setDepartment(department)
                .setDoctorname(doctorname)
                .setDoctorname(doctorname)
                .setHistory(history)
                .setDiagonose(diagonose);

        stub.putStringState(key, JSON.toJSONString(medicalRecord));

        return medicalRecord;
    }

    //删除病历本
    @Transaction
    public MedicalRecord deleteMedicalRecord(final Context ctx, final String key) {

        ChaincodeStub stub = ctx.getStub();
        String recordState = stub.getStringState(key);

        if (StringUtils.isBlank(recordState)) {
            String errorMessage = String.format("MedicalRecord %s does not exist", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }

        stub.delState(key);

        return JSON.parseObject(recordState , MedicalRecord.class);
    }
}
