package com.googol24.mybatis.test;

import com.googol24.mybatis.bean.Department;
import com.googol24.mybatis.bean.Employee;
import com.googol24.mybatis.dao.DepartmentMapper;
import com.googol24.mybatis.dao.EmployeeMapper;
import com.googol24.mybatis.dao.EmployeeMapperAnnotation;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用步骤：
 * 1、Mybatis全局XML配置文件，这个配置文件里面有数据源运行环境信息
 * 2、sql映射文件：配置了每一个sql以及sql的封装规则
 * 3、将sql映射文件注册到全局配置文件里面
 * 4、测试流程：
 * 1) 根据全局配置文件得到SqlSessionFactory对象
 * 2) 使用SqlSession工厂获取到SqlSession对象，使用该对象来进行增删改查调用。一个sqlSession对象就代表个数据库的一次会话，用完要关闭
 * 3) 使用sql的唯一标识（在sql映射文件里面有配置）来告诉mybatis执行那个sql
 */
public class MyBatisTest {
    @Test
    public void test() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession session = sqlSessionFactory.openSession();

        try {
            // 参数1：sql唯一标识
            // 参数2：执行sql需要的参数
            Employee employee = session.selectOne("com.googol24.mybatis.dao.EmployeeMapper.selectOneEmployee", 1);

            System.out.println(employee);

        } finally {
            session.close();
        }
    }

    @Test
    public void testInterface() throws IOException {
        /**
         * 接口式编程：封装了规范（入参个数及类型、返回类型），让 Mybatis 来实现接口与实现的分离
         *
         * 1、更推荐使用接口式编程，利于解耦，更规范
         * 原来：      Dao -> DaoImpl
         * MyBatis:   Mapper -> xxMapper.xml
         *
         * 2、SqlSession对象代表和数据库的一次会话，用完必须关闭
         *    注意：sqlSession对象和connection对象一样都是非线程安全的，所以注意不能作为成员对象使用，每次使用都应该获取新的sqlSession对象
         *
         * 3、Mapper接口没有实现类，但是Mybatis会为这个接口生成一个代理对象（将接口和xml文件绑定）
         *
         * 4、两个重要的配置文件：
         *      Mybatis全局配置文件：包含数据库连接池信息、事务管理器等系统运行环境（该文件可以没有，直接用代码生成SqlSessionFactory对象）
         *      Sql映射文件：保存了每一个sql映射信息，实现了将sql抽取出来（半自动框架）
         */
        // 获取SQLSessionFactory对象
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        // 获取SQLSession对象
        SqlSession session = sqlSessionFactory.openSession();

        try {
            // 使用Mybatis提供的session对象的getMapper方法获取接口的代理实现类的对象
            // Mybatis为接口 EmployeeMapper 自动创建一个代理实现类对象，该代理对象可来执行CURD
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);

            // mapper是一个动态代理对象的类
            System.out.println("class of mapper: " + mapper.getClass());

            // 调用接口的方法，获取需要的Employee对象
            Employee employee = mapper.getEmployeeById(1);

            System.out.println(employee);
        } finally {
            session.close();
        }
    }

    @Test
    public void testAnnotation() throws IOException {
        // 获取SQLSessionFactory对象
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

        // 获取SQLSession对象
        SqlSession session = sqlSessionFactory.openSession();

        try {
            EmployeeMapperAnnotation mapper = session.getMapper(EmployeeMapperAnnotation.class);
            Employee emp = mapper.getEmployeeById(1);

            System.out.println(emp);
        } finally {
            session.close();
        }
    }

    @Test
    // Mybatis允许增删改返回以下类型的返回值：void / Integer / Long / Boolean，直接在接口里面定义返回类型即可
    //
    public void testCURD() throws IOException {
        SqlSessionFactory factory = getSqlSessionFactory();
        // 这里获取到的SqlSession对象不会自动提交，需要自己手动提交
        SqlSession session = factory.openSession();

//        SqlSession session = factory.openSession(true);// 自动提交的SqlSession对象

        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);

            // 测试新增
            Employee employee = new Employee(null, "jerry", "1", "jerry@qq.com");
            mapper.addEmployee(employee);
            System.out.println("添加的自增主键值为：" + employee.getId());

            // 测试修改
//            Employee employee2 = new Employee(3, "timmy", "0", "timmy@qq.com");
//            mapper.updateEmployee(employee2);

            // 测试删除
            int result = mapper.deleteEmployee(3);
            System.out.println("result: " + result);

            // 手动提交数据
            session.commit();

            System.out.println("测试完成");

        } finally {
            session.close();
        }
    }

    @Test
    public void testMultiParams() throws IOException {
        SqlSessionFactory factory = getSqlSessionFactory();
        SqlSession session = factory.openSession(true);

        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            Employee employee = mapper.getEmployeeByIdAndLastName(1, "tom");
            System.out.println(employee);

            // 测试多个参数使用map的情况
            Map<String, Object> map = new HashMap<>();
            map.put("id", 2);
            map.put("lastName", "jerry");
            map.put("tableName", "tbl_employee");
            Employee employeeMap = mapper.getEmployeeByMap(map);
            System.out.println(employeeMap);

            List<Employee> employeeList = mapper.getEmployeesByLastName("%e%");
            for (Employee emp : employeeList) {
                System.out.println(emp);
            }

            Map<String, Object> empMap = mapper.getEmployeeMapById(1);
            System.out.println(empMap);

            Map<Integer, Employee> empMap2 = mapper.getEmployeeMapsByLastName("%r%");
            System.out.println(empMap2);
        } finally {
            session.close();
        }
    }

    @Test
    public void testResultMap() throws IOException {
        SqlSessionFactory factory = getSqlSessionFactory();
        SqlSession session = factory.openSession();

        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            Employee employee = mapper.getEmpById(2);
            System.out.println(employee);
        } finally {
            session.close();
        }
    }

    @Test
    public void testCascade() throws IOException {
        SqlSessionFactory factory = getSqlSessionFactory();
        SqlSession session = factory.openSession();

        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            Employee employee = mapper.getEmpAndDept(1);
            System.out.println(employee);
        } finally {
            session.close();
        }
    }

    @Test
    public void testStep() throws IOException {
        SqlSessionFactory factory = getSqlSessionFactory();
        SqlSession session = factory.openSession();

        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            Employee employee = mapper.getEmpByStep(1);
            System.out.println(employee);
            System.out.println(mapper.getEmpByStep(2));
        } finally {
            session.close();
        }
    }

    @Test
    public void testCollection() throws IOException {
        SqlSessionFactory factory = getSqlSessionFactory();
        SqlSession session = factory.openSession();

        try {
            DepartmentMapper mapper = session.getMapper(DepartmentMapper.class);
            Department department = mapper.getDepartmentByIdPlus(1);

            System.out.println(department);
            System.out.println(department.getEmployees());
        } finally {
            session.close();
        }
    }

    @Test
    public void testStep2() throws IOException {
        SqlSessionFactory factory = getSqlSessionFactory();
        SqlSession session = factory.openSession();

        try {
            DepartmentMapper mapper = session.getMapper(DepartmentMapper.class);
            Department department = mapper.getDepartmentByStep(1);

            System.out.println(department);
            System.out.println(department.getEmployees());
        } finally {
            session.close();
        }
    }

    private SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);

        return new SqlSessionFactoryBuilder().build(inputStream);
    }
}
