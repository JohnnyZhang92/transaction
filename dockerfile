# 使用OpenJDK 21基础镜像
FROM openjdk:21

# 设置工作目录
WORKDIR /app

# 将target目录下的jar包复制到容器内的/app目录
COPY target/transaction-0.0.1-SNAPSHOT.jar app.jar

# 声明运行时容器暴露的端口，这里假设Spring Boot应用监听8080端口
EXPOSE 8080

# 定义容器启动时执行的命令
ENTRYPOINT ["java", "-jar", "/app/app.jar"]