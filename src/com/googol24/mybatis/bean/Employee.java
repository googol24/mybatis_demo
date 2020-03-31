package com.googol24.mybatis.bean;

import org.apache.ibatis.type.Alias;

@Alias("ss")
public class Employee {
    public Employee() {}

    // 有参构造器
    public Employee(Integer id, String lastName, String gender, String email) {
        // 必须带上无参构造器（框架反射需要用到）
//        super();
        this.id = id;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
    }

    private Integer id;

    private String lastName;

    private String gender;

    private String email;

    private Department department;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", department=" + department +
                '}';
    }
}
