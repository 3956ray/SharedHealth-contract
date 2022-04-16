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

    //主治医生
    @Property
    String doctorname;

    //科室
    @Property
    String department;

    //诊断描述
    @Property
    String descript;

    //过往病史
    @Property
    String history;
}
