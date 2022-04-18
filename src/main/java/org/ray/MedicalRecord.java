package org.ray;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;


@DataType
@Data
@Accessors(chain = true)
public class MedicalRecord {
    @Property
    String name;

    @Property
    Integer age;

    @Property
    //出生日期
    String Date;

    @Property
    //家庭地址
    String address;

    //身份证号
    @Property
    String userID;

    //主治医生
    @Property
    String doctorname;

    //科室
    @Property
    String department;

    //过往病史(想使用表格实现)
    @Property
    String history;

    //诊断描述（此次）
    @Property
    String diagonose;
}
