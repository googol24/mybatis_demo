package com.googol24.mybatis.dao;

import com.googol24.mybatis.bean.Employee;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 定义接口，指定每个DB操作的规范
 *
 * 严格的类型检查与返回值检查
 */
public interface EmployeeMapper {

    public Employee getEmployeeById(int id);

    public void addEmployee(Employee employee);

    public void updateEmployee(Employee employee);

    public Integer deleteEmployee(int id);

    public Employee getEmployeeByIdAndLastName(@Param("id") int id, @Param("lastName") String lastName);

    public Employee getEmployeeByMap(Map<String, Object> map);

    public List<Employee> getEmployeesByLastName(String lastName);

    public Map<String, Object> getEmployeeMapById(int id);

    // 告诉Mybatis封装这个Map的时候使用哪个属性作为map的key
    @MapKey("id")
    public Map<Integer, Employee> getEmployeeMapsByLastName(String lastName);

    public Employee getEmpById(int id);

    public Employee getEmpAndDept(int id);

    public Employee getEmpByStep(int id);

    public List<Employee> getEmployeesByDeptId(int deptId);
}
