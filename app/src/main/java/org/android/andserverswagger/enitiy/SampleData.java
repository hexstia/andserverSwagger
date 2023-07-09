package org.android.andserverswagger.enitiy;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = "SampleData")
@ApiModel(value = "SampleData", description = "Pet object that needs to be added to the store")
public class SampleData {
    @ApiModelProperty(required = true, value = "这是一个ID值")
    private Integer id;
    @ApiModelProperty(required = true, value = "这是一个ID值")
    private String name;
    @ApiModelProperty(required = true, value = "这是一个ID值")
    private String email;
    @ApiModelProperty(required = true, value = "这是一个ID值")
    private Integer age;
    @ApiModelProperty(required = true, value = "这是一个ID值")
    private Date dateOfBirth;

    public SampleData(Integer id, String name, String email, Integer age, Date dateOfBirth) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
    }

    @XmlElement(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlElement(name = "age")
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @XmlElement(name = "dateOfBirth")
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
