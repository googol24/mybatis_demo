# Mybatis 

> 参数处理

1 单个参数：Mybatis不会做特殊处理，使用#{参数名}，这个参数名可以任意指定

2 多个参数：Mybatis会做特殊处理。多个参数会被封装成一个map(key:param1 ... paramN (或者使用参数的索引); value:传入的参数值)
- 异常： org.apache.ibatis.binding.BindingException: Parameter 'id' not found. Available parameters are [0, 1, param1, param2]
- 操作：
       方法：public Employee getEmployeeByIdAndLastName(int id, String lastName);
       取值：#{id}、#{lastName} 对应于 #{param1} #{param2} 或者 #{0} #{1}
       
3 命名参数（多个参数时推荐使用）：使用@Param注解明确指定封装参数时map的key @Param("id")   
    多个参数会被封装成一个map(key:使用@Param注解指定的值; value:传入的参数值) 
    使用${#参数名}就可以取出对应的参数值  

4 如果多个参数正好是我们业务逻辑的数据模型的数据，我们直接传入POJO即可（推荐使用）
    #{属性名}：取出传入的POJO的属性值
  如果多个参数不是我们业务逻辑的数据模型的数据，没有对应的POJO，为了方便我们也可以传入map（不经常使用的情况）
    #{key}：取出map中对应key的value
  如果多个参数不是POJO中的数据，但是经常要使用，推荐编写一个TO的数据传输对象（Transfer Object），例如Page对象
  
  
> 参数处理的源码解读  


> $ vs #

 注意：#{} ${}都可以获取参数值（Map中的值）或者POJO的属性值
 区别：#{}以预编译的形式将参数设置到sql中（类似于JDBC中的PreparedStatement），可以防止sql注入; ${}取出的值直接拼装近sql语句中；无法防止sql注入，有安全问题
 大多数情况下，获取参数的值都使用#{}；某些情况下可能使用${}（原生JDBC不支持占位符的地方）
 比如分表的时候(按照年份拆分)：select * from salary_${year} WHERE ...
 比如根据指定字段进行排序：select * from tbl_employee order by ${column} 